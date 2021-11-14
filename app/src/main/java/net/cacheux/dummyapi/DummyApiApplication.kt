package net.cacheux.dummyapi

import android.app.Application
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.datasource.cached.DatasourceRepositoryCachedImpl
import net.cacheux.dummyapi.datasource.room.DatasourceRepositoryRoomImpl
import net.cacheux.dummyapi.datasource.server.DatasourceRepositoryServerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DummyApiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                module {
                    single<DatasourceRepository> {
                        DatasourceRepositoryCachedImpl(
                            DatasourceRepositoryServerImpl(BuildConfig.API_URL, BuildConfig.API_KEY),
                            DatasourceRepositoryRoomImpl(androidContext())
                        )
                    }
                }
            )
        }
    }
}