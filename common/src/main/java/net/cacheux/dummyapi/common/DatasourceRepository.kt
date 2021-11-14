package net.cacheux.dummyapi.common

import kotlinx.coroutines.flow.Flow
import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User

/**
 * Base interface for a datasource repository, which must be able to retrieve a user list and user details
 */
interface DatasourceRepository {
    fun getUserList(page: Int = 0, size: Int = 10): Flow<List<User>>
    fun getDetailedUser(uid: String): Flow<DetailedUser?>
}