package net.cacheux.dummyapi.datasource.server

import net.cacheux.dummyapi.datasource.server.json.JsonDetailedUser
import net.cacheux.dummyapi.datasource.server.json.JsonResult
import net.cacheux.dummyapi.datasource.server.json.JsonUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyApiUsers {

    @GET("user")
    suspend fun getUserList(@Query("page") page: Int, @Query("limit") limit: Int): JsonResult<JsonUser>

    @GET("user/{uid}")
    suspend fun getUserDetails(@Path("uid") uid: String): JsonDetailedUser
}