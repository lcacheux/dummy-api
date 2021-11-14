package net.cacheux.dummyapi.datasource.test

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DatasourceRepositoryTestImplTest {
    @Test
    fun testDatasourceRepositoryTestImpl() {
        val datasourceRepository = DatasourceRepositoryTestImpl()

        runBlocking {
            assertEquals(10, datasourceRepository.getUserList(0, 10).first().size)
            assertEquals("id24", datasourceRepository.getUserList(3, 8).first()[0].id)

            assertEquals("id123", datasourceRepository.getDetailedUser("id123").first()?.id)
            assertNull(datasourceRepository.getDetailedUser("abcd").first())
        }
    }
}