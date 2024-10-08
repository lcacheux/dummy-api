package net.cacheux.dummyapi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import net.cacheux.dummyapi.MainViewModel
import net.cacheux.dummyapi.R
import net.cacheux.dummyapi.common.model.User

@Composable
fun LiveUserList(viewModel: MainViewModel) {
    UserList(viewModel)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserList(viewModel: MainViewModel) {
    Scaffold(
        content = {
            val lazyUserList: LazyPagingItems<User> = viewModel.users.collectAsLazyPagingItems()
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                items(count = lazyUserList.itemCount) { index ->
                    val user = lazyUserList[index]
                    user?.let { UserListItem(viewModel, user) }
                }

                lazyUserList.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillParentMaxSize()) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            ) }
                        }
                        loadState.refresh is LoadState.Error -> {
                            item {
                                Box(modifier = Modifier.fillParentMaxSize()) {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(8.dp),
                                            text = stringResource(id = R.string.error_loading_userlist)
                                        )
                                        OutlinedButton(
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            onClick = { retry() }
                                        ) {
                                            Text(text = stringResource(R.string.retry))
                                        }
                                    }

                                }
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            item {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(onClick = { retry() }
                                    ) {
                                        Text(text = stringResource(R.string.retry))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}