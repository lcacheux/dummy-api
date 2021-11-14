package net.cacheux.dummyapi.datasource.room

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import net.cacheux.dummyapi.common.MutableDatasourceRepository
import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User
import net.cacheux.dummyapi.datasource.room.entity.RoomDetailedUser
import net.cacheux.dummyapi.datasource.room.entity.RoomUser
import net.cacheux.dummyapi.datasource.room.entity.toRoomDetailedUser
import net.cacheux.dummyapi.datasource.room.entity.toRoomUser
import kotlin.coroutines.CoroutineContext

class DatasourceRepositoryRoomImpl(
    context: Context,
    private val db: Database = Room.databaseBuilder(context, Database::class.java, "dummyapi").build(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO
): MutableDatasourceRepository {
    override suspend fun saveUserList(userList: List<User>) {
        withContext(coroutineContext) {
            db.userDao().addUsers(userList.map { it.toRoomUser() })
        }
    }

    override suspend fun saveDetailedUser(detailedUser: DetailedUser) {
        withContext(coroutineContext) {
            db.detailedUserDao().addDetailedUser(detailedUser.toRoomDetailedUser())
        }
    }

    override suspend fun clearAllData() {
        withContext(coroutineContext) {
            db.clearAllTables()
        }
    }

    override fun getUserList(page: Int, size: Int) =
        db.userDao().getUserList(page, size).map { userList ->
            userList.map { it.toUser() }
        }.flowOn(coroutineContext)

    override fun getDetailedUser(uid: String) = flow {
        val result = db.detailedUserDao().getDetailedUser(uid).first()
        result?.let {
            emit(it.toDetailedUser())
        } ?: emit(null)
    }.flowOn(coroutineContext)
}

fun RoomUser.toUser() = User(
    index = index,
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    pictureUrl = url
)

fun RoomDetailedUser.toDetailedUser() = DetailedUser(
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    pictureUrl = url,
    gender = gender,
    email = email,
    dateOfBirth = dateOfBirth,
    phone = phone,
    street = street,
    city = city,
    state = state,
    country = country,
    timezone = timezone,
    registerDate = registerDate,
    updatedDate = updatedDate
)