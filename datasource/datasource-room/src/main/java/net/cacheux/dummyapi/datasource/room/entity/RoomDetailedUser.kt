package net.cacheux.dummyapi.datasource.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.cacheux.dummyapi.common.model.DetailedUser

@Entity(tableName = "detailed_user")
data class RoomDetailedUser(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "gender") val gender: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,

    @ColumnInfo(name = "street") val street: String? = null,
    @ColumnInfo(name = "city") val city: String? = null,
    @ColumnInfo(name = "state") val state: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "timezone") val timezone: String? = null,

    @ColumnInfo(name = "register_date") val registerDate: String? = null,
    @ColumnInfo(name = "updated_date") val updatedDate: String? = null
)

fun DetailedUser.toRoomDetailedUser() = RoomDetailedUser(
    id = id,
    title = title,
    firstName = firstName,
    lastName = lastName,
    url = pictureUrl,
    gender = gender,
    email = email,
    dateOfBirth = dateOfBirth,
    phone = phone,
    street = street,
    city = city,
    state = state,
    country = country,
    timezone = timezone,
    registerDate = registerDate,
    updatedDate = updatedDate
)