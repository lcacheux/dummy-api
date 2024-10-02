package net.cacheux.dummyapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import net.cacheux.dummyapi.MainViewModel.ViewState
import net.cacheux.dummyapi.ui.DetailedUserView
import net.cacheux.dummyapi.ui.LiveUserList
import net.cacheux.dummyapi.ui.theme.DummyAPITheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.getMessage().collectAsState()
            val context = LocalContext.current

            DummyAPITheme {
                DummyApiApp(viewModel)
            }

            LaunchedEffect(key1 = state.value) {
                if (state.value != 0)
                    Toast.makeText(context, state.value, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyApiApp(viewModel: MainViewModel) {
    val viewState = viewModel.getViewState().collectAsState()
    val isCacheAvailable = viewModel.isCacheAvailable()
    var showMenu by remember { mutableStateOf(false) }

    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = if (isCacheAvailable) {
                    {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.clearCache()
                                    showMenu = false
                                },
                                text = { Text(text = stringResource(id = R.string.clear_cache)) }
                            )
                        }
                    }
                } else {
                    {}
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LiveUserList(viewModel)

            if (viewState.value !is ViewState.ShowUserList) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.closeDetails() },
                    sheetState = bottomSheetState
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(4.dp)
                    ) {
                        viewState.value.let {
                            when (it) {
                                is ViewState.LoadUser ->
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                                is ViewState.UserLoadedSuccess -> {
                                    DetailedUserView(user = it.user)
                                }

                                is ViewState.UserLoadedError -> Column(
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(8.dp),
                                        text = stringResource(id = R.string.error_loading_detailed_user)
                                    )
                                    OutlinedButton(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        onClick = { viewModel.reloadSelectedUser() }
                                    ) {
                                        Text(text = stringResource(R.string.retry))
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
