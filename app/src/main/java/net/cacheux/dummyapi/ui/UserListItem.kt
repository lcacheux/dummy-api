package net.cacheux.dummyapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import net.cacheux.dummyapi.MainViewModel
import net.cacheux.dummyapi.common.model.User
import net.cacheux.dummyapi.ui.theme.Purple200

@Composable
fun UserListItem(viewModel: MainViewModel, user: User, callback: (User) -> Unit) {
    val selectedUser = viewModel.getSelectedUser().observeAsState()
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        elevation = if (selectedUser.value == user) 4.dp else 2.dp,
        backgroundColor = if (selectedUser.value == user) Purple200 else Color.White,
    ) {
        Row(
            Modifier.clickable {
                callback(user)
            }
        ) {
            Image(
                painter = rememberImagePainter(user.pictureUrl),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)) {
                Text(text = "${user.title} ${user.firstName} ${user.lastName}", style = typography.h6)
            }
        }
    }
}