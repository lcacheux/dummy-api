package net.cacheux.dummyapi.datasource.memory

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.cacheux.dummyapi.datasource.test.generateDetailedUser
import net.cacheux.dummyapi.datasource.test.generateUserList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DatasourceRepositoryMemoryImplTest {

    @Test
    fun testUserList() {
        val datasource = DatasourceRepositoryMemoryImpl()

        runBlocking {
            assertEquals(0, datasource.getUserList(0, 10).first().size)

            datasource.saveUserList(generateUserList(0, 20))

            assertEquals(10, datasource.getUserList(0, 10).first().size)
            assertEquals(20, datasource.getUserList(0, 20).first().size)

            // Ensure that no duplicate are inserted
            datasource.saveUserList(generateUserList(0, 30))

            assertEquals(30, datasource.getUserList(0, 60).first().size)

            assertEquals("id10", datasource.getUserList(1, 10).first()[0].id)
        }
    }

    @Test
    fun testUserListOrder() {
        val datasource = DatasourceRepositoryMemoryImpl()

        runBlocking {
            datasource.saveUserList(generateUserList(0, 10)) // Save ids 0 - 9
            datasource.saveUserList(generateUserList(2, 10)) // Save ids 20 - 29

            val values1 = datasource.getUserList(0, 30).first() // Retrieve all
            assertEquals(20, values1.size)
            assertEquals(20, values1[10].index)
            assertEquals("id20", values1[10].id)

            datasource.saveUserList(generateUserList(1, 10)) // Save ids 10 - 19
            val values2 = datasource.getUserList(0, 30).first() // Retrieve all
            assertEquals(30, values2.size)
            assertEquals(15, values2[15].index)
            assertEquals("id15", values2[15].id)
            assertEquals(20, values2[20].index)
            assertEquals("id20", values2[20].id)
        }
    }

    @Test
    fun testDetailedUser() {
        val datasource = DatasourceRepositoryMemoryImpl()

        runBlocking {
            assertNull(datasource.getDetailedUser("1234").first())

            datasource.saveDetailedUser(generateDetailedUser("1234"))

            assertEquals("1234", datasource.getDetailedUser("1234").first()?.id)
        }
    }

    @Test
    fun testSafeSubList() {
        val list = (0 until 10).toList()

        assertEquals(10, list.size)
        assertEquals(10, list.safeSubList(0, 50).size)
        assertEquals(0, list.safeSubList(50, 60).size)
        assertEquals(4, list.safeSubList(4, 8).size)
        assertEquals(4, list.safeSubList(4, 8)[0])
    }
}