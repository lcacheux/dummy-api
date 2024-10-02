package net.cacheux.dummyapi.datasource.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.cacheux.dummyapi.datasource.test.generateDetailedUser
import net.cacheux.dummyapi.datasource.test.generateUserList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatasourceRepositoryRoomImplTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = Room
        .inMemoryDatabaseBuilder(context, Database::class.java)
        .allowMainThreadQueries()
        .build()

    @Test
    fun testDatasourceRepositoryRoomImpl() {
        val datasourceRoom = DatasourceRepositoryRoomImpl(context, db)

        runBlocking {
            datasourceRoom.saveUserList(generateUserList(0, 50))
            assertEquals(50, db.userDao().getUserCount())
            assertEquals(0, datasourceRoom.getUserList(10, 10).first().size)
            assertEquals(10, datasourceRoom.getUserList(0, 10).first().size)
            assertEquals("id20", datasourceRoom.getUserList(4, 5).first()[0].id)

            // Ensure that duplicates are not saved twice
            datasourceRoom.saveUserList(generateUserList(2, 20))
            assertEquals(60, db.userDao().getUserCount())
            assertEquals(0, datasourceRoom.getUserList(6, 10).first().size)
        }
    }

    @Test
    fun testDetailedUser() {
        val datasourceRoom = DatasourceRepositoryRoomImpl(context, db)

        runBlocking {
            datasourceRoom.saveDetailedUser(generateDetailedUser("1234"))
            assertNotNull(datasourceRoom.getDetailedUser("1234").first())
            assertEquals("Georges", datasourceRoom.getDetailedUser("1234").first()?.firstName)
            assertNull(datasourceRoom.getDetailedUser("4321").first()) // User not found
        }

    }
}