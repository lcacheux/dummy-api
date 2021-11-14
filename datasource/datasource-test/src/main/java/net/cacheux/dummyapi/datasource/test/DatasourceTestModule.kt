package net.cacheux.dummyapi.datasource.test

import net.cacheux.dummyapi.common.DatasourceRepository
import org.koin.dsl.module

val datasourceTestModule = module {
    single<DatasourceRepository> { DatasourceRepositoryTestImpl() }
}