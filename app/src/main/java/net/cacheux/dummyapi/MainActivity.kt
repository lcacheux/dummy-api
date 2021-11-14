package net.cacheux.dummyapi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.cacheux.dummyapi.MainViewModel.ViewState
import net.cacheux.dummyapi.ui.DetailedUserView
import net.cacheux.dummyapi.ui.LiveUserList
import net.cacheux.dummyapi.ui.theme.DummyAPITheme

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DummyAPITheme {
                DummyApiApp(viewModel)
            }
        }

        viewModel.getMessage().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun DummyApiApp(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val viewState = viewModel.getViewState().observeAsState()
    val isCacheAvailable = viewModel.isCacheAvailable().observeAsState()
    var showMenu by remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { bottomSheetValue ->
                if (bottomSheetValue == BottomSheetValue.Collapsed) {
                    viewModel.closeDetails()
                }
                true
            }
        )
    )
    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = if (isCacheAvailable.value == true) {{
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.clearCache()
                            showMenu = false
                        }) {
                            Text(text = stringResource(id = R.string.clear_cache))
                        }
                    }
                }} else {{}}
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(4.dp)
            ) {
                viewState.value?.let {
                    when(it) {
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
        },
        sheetPeekHeight = 0.dp
    ) {
        LiveUserList(viewModel)

        when(viewState.value) {
            is ViewState.ShowUserList ->
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            else ->
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
        }
    }
}