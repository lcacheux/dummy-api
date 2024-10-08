package net.cacheux.dummyapi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.cacheux.dummyapi.common.model.DetailedUser

@Composable
fun DetailedUserView(user: DetailedUser) {
    Row {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${user.title} ${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "${user.email}", style = MaterialTheme.typography.titleSmall)
            Text(text = "${user.phone}", style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DetailedUserView(user = DetailedUser(
        id = "1234",
        title = "Mr",
        firstName = "Georges",
        lastName = "Abitbol",
        email = "georges@abitbol.com",
        phone = "+33 (0) 6 11 22 33 44"
    ))
}