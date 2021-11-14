package net.cacheux.dummyapi.datasource.test

import kotlinx.coroutines.flow.flowOf
import net.cacheux.dummyapi.common.DatasourceRepository

/**
 * Wrap the utility methods into a fully functional DatasourceRepository
 */
class DatasourceRepositoryTestImpl : DatasourceRepository {
    override fun getUserList(page: Int, size: Int) = flowOf(generateUserList(page, size))
    override fun getDetailedUser(uid: String) = flowOf(
        // Only ids starting with proper prefix return a value, to be able to test invalid keys
        if (uid.startsWith("id")) {
            generateDetailedUser(uid)
        } else {
            null
        }

    )
}