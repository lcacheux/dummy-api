package net.cacheux.dummyapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val selectedUser = viewModel.getSelectedUser().collectAsState()
    val isSelected = selectedUser.value == user
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Purple200 else Color.White),
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
                Text(text = "${user.title} ${user.firstName} ${user.lastName}", style = typography.titleSmall)
            }
        }
    }
}