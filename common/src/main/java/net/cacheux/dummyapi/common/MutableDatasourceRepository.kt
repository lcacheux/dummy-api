package net.cacheux.dummyapi.common

import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User

/**
 * Mutable datasource add methods to save a user list and user details
 */
interface MutableDatasourceRepository: DatasourceRepository {
    suspend fun saveUserList(userList: List<User>)
    suspend fun saveDetailedUser(detailedUser: DetailedUser)
    suspend fun clearAllData()
}