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
fun UserListItem(viewModel: MainViewModel, user: User) {
    val selectedUser = viewModel.getSelectedUser().observeAsState()
    val isSelected = selectedUser.value == user
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp).fillMaxWidth(),
        elevation = if (isSelected) 4.dp else 2.dp,
        backgroundColor = if (isSelected) Purple200 else Color.White,
    ) {
        Row(
            Modifier.clickable {
                if (isSelected) {
                    viewModel.closeDetails()
                } else {
                    viewModel.loadUser(user)
                }
            }
        ) {
            Image(
                painter = rememberImagePainter(
                    data = user.pictureUrl,
                    builder = {
                        crossfade(true)
                    }
                ),
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