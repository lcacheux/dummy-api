package net.cacheux.dummyapi.common

/**
 * A datasource repository with cache features, with an option to clear the existing cache
 */
interface CachedDatasourceRepository: DatasourceRepository {
    suspend fun clearCache()
}