package net.cacheux.dummyapi.datasource.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User
import net.cacheux.dummyapi.datasource.server.json.JsonDetailedUser
import net.cacheux.dummyapi.datasource.server.json.JsonUser
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@ExperimentalSerializationApi
class DatasourceRepositoryServerImpl(
    baseUrl: String,
    appId: String = "",
    private val coroutineContext: CoroutineContext = Dispatchers.IO
): DatasourceRepository {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("app-id", appId)
                .method(original.method(), original.body())
                .build();
            chain.proceed(request);
        }
        .build()

    private val contentType = MediaType.get("application/json")
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(httpClient)
        .build()

    private val dummyApiUsers = retrofit.create(DummyApiUsers::class.java)

    override fun getUserList(page: Int, size: Int) = flow {
        dummyApiUsers.getUserList(page, size).data?.let { userList ->
            emit (userList.mapIndexed { index, user -> user.toUser(index + page * size) })
        } ?: throw Exception()
    }.flowOn(coroutineContext)

    override fun getDetailedUser(uid: String) = flow {
        try {
            val result = dummyApiUsers.getUserDetails(uid)
            emit (result.toDetailedUser())
        } catch (e: IOException) {
            emit(null)
        } catch (e: SerializationException) {
            emit(null)
        }
    }.flowOn(coroutineContext)
}

fun JsonUser.toUser(index: Int) = User(
    index = index,
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    pictureUrl = picture
)

fun JsonDetailedUser.toDetailedUser() = DetailedUser(
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    pictureUrl = picture,
    gender = gender,
    email = email,
    dateOfBirth = dateOfBirth,
    phone = phone,
    street = location?.street,
    city = location?.city,
    state = location?.state,
    country = location?.country,
    timezone = location?.timezone,
    registerDate = registerDate,
    updatedDate = updatedDate
)