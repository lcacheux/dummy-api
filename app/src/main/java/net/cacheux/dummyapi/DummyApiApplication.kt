package net.cacheux.dummyapi

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.datasource.cached.DatasourceRepositoryCachedImpl
import net.cacheux.dummyapi.datasource.room.DatasourceRepositoryRoomImpl
import net.cacheux.dummyapi.datasource.server.DatasourceRepositoryServerImpl
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
    @Singleton
    @Provides
    fun provideDatasource(@ApplicationContext context: Context): DatasourceRepository {
        return DatasourceRepositoryCachedImpl(
            DatasourceRepositoryServerImpl(BuildConfig.API_URL, BuildConfig.API_KEY),
            DatasourceRepositoryRoomImpl(context)
        )
    }

    @Singleton
    @Provides
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO
}

@HiltAndroidApp
class DummyApiApplication: Application()