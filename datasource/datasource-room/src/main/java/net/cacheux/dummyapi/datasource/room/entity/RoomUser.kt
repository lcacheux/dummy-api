package net.cacheux.dummyapi.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.cacheux.dummyapi.common.model.User

@Entity(tableName = "user")
data class RoomUser(
    @PrimaryKey var index: Int,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "url") val url: String?
)

fun User.toRoomUser() = RoomUser(
    index = index,
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    url = pictureUrl
)