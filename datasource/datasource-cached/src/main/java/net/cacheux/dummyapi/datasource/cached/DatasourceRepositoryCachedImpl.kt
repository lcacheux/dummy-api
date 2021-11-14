package net.cacheux.dummyapi.datasource.cached

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import net.cacheux.dummyapi.common.CachedDatasourceRepository
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.common.MutableDatasourceRepository

class DatasourceRepositoryCachedImpl(
    private val originalDatasource: DatasourceRepository,
    private val cacheDatasource: MutableDatasourceRepository
) : CachedDatasourceRepository {

    override suspend fun clearCache() {
        cacheDatasource.clearAllData()
    }

    override fun getUserList(page: Int, size: Int) = flow {
        val firstIndex = page * size
        val lastIndex = (page + 1) * size - 1
        val cachedList = cacheDatasource.getUserList(page, size).first()
        emit(
            if (cachedList.size < size || cachedList.first().index < firstIndex || cachedList.last().index > lastIndex) {
                originalDatasource.getUserList(page, size).first().also {
                    cacheDatasource.saveUserList(it)
                }
            } else cachedList
        )
    }

    override fun getDetailedUser(uid: String) = flow {
        emit(
            cacheDatasource.getDetailedUser(uid).first() ?:
            originalDatasource.getDetailedUser(uid).first()?.also {
                cacheDatasource.saveDetailedUser(it)
            }
        )
    }
}