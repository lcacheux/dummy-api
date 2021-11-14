package net.cacheux.dummyapi.datasource.memory

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.flowOf
import net.cacheux.dummyapi.common.MutableDatasourceRepository
import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User

class DatasourceRepositoryMemoryImpl: MutableDatasourceRepository {
    @VisibleForTesting
    val userList = mutableListOf<User>()

    @VisibleForTesting
    val detailsMap = mutableMapOf<String, DetailedUser>()

    override suspend fun saveUserList(userList: List<User>) {
        this.userList.addAll(userList.filter { user -> user.id !in this.userList.map { it.id } })
        this.userList.sortWith { t, t2 -> t.index - t2.index }
    }

    override suspend fun saveDetailedUser(detailedUser: DetailedUser) {
        detailsMap[detailedUser.id] = detailedUser
    }

    override suspend fun clearAllData() {
        userList.clear()
        detailsMap.clear()
    }

    override fun getUserList(page: Int, size: Int) = flowOf(
        userList.safeSubList(page * size, (page + 1) * size)
    )

    override fun getDetailedUser(uid: String) = flowOf(detailsMap[uid])
}

/**
 * Ensure the subList never throw IndexOutOfBoundException
 */
fun <T> List<T>.safeSubList(from: Int, to: Int): List<T> =
    if (size > from) {
        if (size < to) {
            subList(from, size)
        } else {
            subList(from, to)
        }
    } else emptyList()