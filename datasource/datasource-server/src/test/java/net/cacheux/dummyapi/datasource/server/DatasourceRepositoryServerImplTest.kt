package net.cacheux.dummyapi.datasource.server

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import net.cacheux.dummyapi.datasource.server.MockDummyApiServer.Companion.TEST_PORT
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.ConnectException

@ExperimentalSerializationApi
class DatasourceRepositoryServerImplTest {

    private val server = MockDummyApiServer()

    @Before
    fun init() {
        server.startMockWebServer()
    }

    @After
    fun shutdown() {
        server.stopMockWebServer()
    }

    @Test
    fun testDatasourceRepositoryServerImpl() {
        val datasourceRepository = DatasourceRepositoryServerImpl("http://localhost:$TEST_PORT")

        runBlocking {
            assertEquals(10, datasourceRepository.getUserList().first().size)
            assertEquals("Andersen", datasourceRepository.getDetailedUser("123").first()?.lastName)
        }
    }

    @Test(expected = ConnectException::class)
    fun testServerUnavailable() {
        val datasourceRepository = DatasourceRepositoryServerImpl("http://localhost:${TEST_PORT+2}")

        runBlocking {
            datasourceRepository.getUserList().first()
        }
    }
}