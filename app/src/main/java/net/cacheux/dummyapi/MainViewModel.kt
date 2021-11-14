package net.cacheux.dummyapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.cacheux.dummyapi.common.CachedDatasourceRepository
import net.cacheux.dummyapi.common.DatasourceRepository
import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val coroutineContext: CoroutineContext = Dispatchers.IO) : ViewModel(), KoinComponent {
    companion object {
        const val PAGE_SIZE = 20
    }

    sealed class ViewState {
        object ShowUserList: ViewState()
        data class LoadUser(val user: User): ViewState()
        data class UserLoadedSuccess(val user: DetailedUser): ViewState()
        data class UserLoadedError(val error: Int): ViewState()
    }

    private val datasource: DatasourceRepository by inject()

    private val viewState = MutableLiveData<ViewState>(ViewState.ShowUserList)
    fun getViewState(): LiveData<ViewState> = viewState

    private val selectedUser = MutableLiveData<User?>()
    fun getSelectedUser(): LiveData<User?> = selectedUser

    private val message = MutableLiveData<Int>()
    fun getMessage(): LiveData<Int> = message

    fun isCacheAvailable() = object : LiveData<Boolean>(datasource is CachedDatasourceRepository) {}

    val users: Flow<PagingData<User>> = Pager(PagingConfig(initialLoadSize = PAGE_SIZE, pageSize = PAGE_SIZE)) {
        userSource
    }.flow

    fun closeDetails() {
        viewState.value = ViewState.ShowUserList
        selectedUser.value = null
    }

    fun loadUser(user: User) {
        selectedUser.value = user
        viewState.value = ViewState.LoadUser(user)
        viewModelScope.launch(context = coroutineContext) {
            datasource.getDetailedUser(user.id).first()?.let { user ->
                viewState.postValue(ViewState.UserLoadedSuccess(user))
            } ?: run {
                viewState.postValue(ViewState.UserLoadedError(R.string.error_loading_detailed_user))
            }
        }
    }

    fun reloadSelectedUser() = selectedUser.value?.let { loadUser(it) }

    fun clearCache() {
        if (datasource is CachedDatasourceRepository) {
            viewModelScope.launch(context = coroutineContext) {
                (datasource as CachedDatasourceRepository).clearCache()
                message.postValue(R.string.cache_cleared)
            }
        }
    }

    private val userSource = object: PagingSource<Int, User>() {
        override fun getRefreshKey(state: PagingState<Int, User>) = state.anchorPosition

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
            return try {
                val nextPage = params.key ?: 1
                val userListResponse = datasource.getUserList(page = nextPage - 1, size = params.loadSize)

                LoadResult.Page(
                    data = userListResponse.first(),
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = nextPage + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

    }
}