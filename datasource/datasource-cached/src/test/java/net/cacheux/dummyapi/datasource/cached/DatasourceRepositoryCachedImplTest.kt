package net.cacheux.dummyapi.datasource.cached

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.datasource.memory.DatasourceRepositoryMemoryImpl
import net.cacheux.dummyapi.datasource.test.DatasourceRepositoryTestImpl
import org.junit.Assert.*
import org.junit.Test

class DatasourceRepositoryCachedImplTest {
    @Test
    fun testUserList() {
        val datasourceTest = DatasourceRepositoryTestImpl()
        val datasourceMemory = DatasourceRepositoryMemoryImpl()
        val mockDatasourceTest = mockDatasourceRepository(datasourceTest)

        val datasource = DatasourceRepositoryCachedImpl(mockDatasourceTest, datasourceMemory)

        runBlocking {
            assertEquals(10, datasource.getUserList(0, 10).first().size)

            verify(mockDatasourceTest, times(1)).getUserList(any(), any())
            assertEquals(10, datasourceMemory.userList.size)

            // Next call on the same interval should not trigger original datasource again
            assertEquals(10, datasource.getUserList(0, 10).first().size)
            verify(mockDatasourceTest, times(1)).getUserList(any(), any())
        }
    }

    /**
     * If first requests haven't been consecutive, ensure that later requests in empty interval are done correctly
     */
    @Test
    fun testUserListSlicedCache() {
        val datasourceTest = DatasourceRepositoryTestImpl()
        val datasourceMemory = DatasourceRepositoryMemoryImpl()
        val mockDatasourceTest = mockDatasourceRepository(datasourceTest)

        val datasource = DatasourceRepositoryCachedImpl(mockDatasourceTest, datasourceMemory)

        runBlocking {
            // Retrieve to separate slices of the list
            val slice1 = datasource.getUserList(0, 10).first() // Indexes 0 - 9
            val slice2 = datasource.getUserList(2, 10).first() // Indexes 20 - 29
            assertEquals("id9", slice1.last().id)
            assertEquals("id20", slice2.first().id)
            verify(mockDatasourceTest, times(2)).getUserList(any(), any())

            val slice3 = datasource.getUserList(1, 8).first() // Indexes 8 - 15
            verify(mockDatasourceTest, times(3)).getUserList(any(), any())
            assertEquals(8, slice3.size)
            assertEquals("id8", slice3.first().id)
            assertEquals("id15", slice3.last().id)

            // Now cache should contain [0-15] [20-29]
            val slice4 = datasource.getUserList(1, 7).first() // Indexes 7 - 13
            assertEquals("id7", slice4.first().id)
            assertEquals("id13", slice4.last().id)
            // Already in cache so should not have been retrieved remotely
            verify(mockDatasourceTest, times(3)).getUserList(any(), any())

            val slice5 = datasource.getUserList(6, 3).first() // Indexes 18 - 20
            verify(mockDatasourceTest, times(4)).getUserList(any(), any())
            assertEquals("id18", slice5.first().id)
            assertEquals("id20", slice5.last().id)
        }
    }

    @Test
    fun testDetailedUser() {
        val datasourceTest = DatasourceRepositoryTestImpl()
        val datasourceMemory = DatasourceRepositoryMemoryImpl()
        val mockDatasourceTest = mockDatasourceRepository(datasourceTest)

        val datasource = DatasourceRepositoryCachedImpl(mockDatasourceTest, datasourceMemory)

        runBlocking {
            assertEquals("id1234", datasource.getDetailedUser("id1234").first()?.id)

            verify(mockDatasourceTest, times(1)).getDetailedUser(any())
            assertTrue(datasourceMemory.detailsMap.containsKey("id1234"))

            // Next call to the same id should not trigger original datasource again
            assertEquals("id1234", datasource.getDetailedUser("id1234").first()?.id)
            verify(mockDatasourceTest, times(1)).getDetailedUser(any())
        }
    }

    private fun mockDatasourceRepository(datasource: DatasourceRepository) = mock<DatasourceRepository> {
        on { getUserList(any(), any()) }.thenAnswer {
            datasource.getUserList(it.getArgument(0), it.getArgument(1))
        }
        on { getDetailedUser(any()) }.thenAnswer {
            datasource.getDetailedUser(it.getArgument(0))
        }
    }
}