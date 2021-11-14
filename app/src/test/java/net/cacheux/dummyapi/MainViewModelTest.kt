package net.cacheux.dummyapi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.common.model.User
import net.cacheux.dummyapi.datasource.cached.DatasourceRepositoryCachedImpl
import net.cacheux.dummyapi.datasource.memory.DatasourceRepositoryMemoryImpl
import net.cacheux.dummyapi.datasource.test.DatasourceRepositoryTestImpl
import net.cacheux.dummyapi.datasource.test.datasourceTestModule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class MainViewModelTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(datasourceTestModule)
    }

    @Test
    fun testSelectedUser() {
        val viewModel = MainViewModel(testCoroutineDispatcher)

        assertNull(viewModel.getSelectedUser().value)
        assertTrue(viewModel.getViewState().value is MainViewModel.ViewState.ShowUserList)

        viewModel.loadUser(User(index = 1, id = "id1234"))
        assertEquals("id1234", viewModel.getSelectedUser().value?.id)
        assertTrue(viewModel.getViewState().value is MainViewModel.ViewState.UserLoadedSuccess)

        viewModel.closeDetails()
        assertNull(viewModel.getSelectedUser().value)
        assertTrue(viewModel.getViewState().value is MainViewModel.ViewState.ShowUserList)
    }

    @Test
    fun testCacheDisabled() {
        val viewModel = MainViewModel(testCoroutineDispatcher)

        assertFalse(viewModel.isCacheAvailable().value == true)

        viewModel.clearCache()
        assertNull(viewModel.getMessage().value)
    }

    @Test
    fun testCacheEnabled() {
        loadKoinModules(module {
            single<DatasourceRepository> {
                DatasourceRepositoryCachedImpl(
                    DatasourceRepositoryTestImpl(),
                    DatasourceRepositoryMemoryImpl()
                )
            }
        })

        val viewModel = MainViewModel(testCoroutineDispatcher)

        assertTrue(viewModel.isCacheAvailable().value == true)

        viewModel.clearCache()
        assertEquals(R.string.cache_cleared, viewModel.getMessage().value)
    }
}