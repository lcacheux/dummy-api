package net.cacheux.dummyapi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.cacheux.dummyapi.common.model.User
import net.cacheux.dummyapi.datasource.cached.DatasourceRepositoryCachedImpl
import net.cacheux.dummyapi.datasource.memory.DatasourceRepositoryMemoryImpl
import net.cacheux.dummyapi.datasource.test.DatasourceRepositoryTestImpl
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class MainViewModelTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Test
    fun testSelectedUser() {
        val viewModel = MainViewModel(testCoroutineDispatcher, DatasourceRepositoryTestImpl())

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
        val viewModel = MainViewModel(testCoroutineDispatcher, DatasourceRepositoryTestImpl())

        assertFalse(viewModel.isCacheAvailable().value == true)

        viewModel.clearCache()
        assertNull(viewModel.getMessage().value)
    }

    @Test
    fun testCacheEnabled() {
        val viewModel = MainViewModel(testCoroutineDispatcher, DatasourceRepositoryCachedImpl(
            DatasourceRepositoryTestImpl(),
            DatasourceRepositoryMemoryImpl()
        ))

        assertTrue(viewModel.isCacheAvailable().value == true)

        viewModel.clearCache()
        assertEquals(R.string.cache_cleared, viewModel.getMessage().value)
    }
}