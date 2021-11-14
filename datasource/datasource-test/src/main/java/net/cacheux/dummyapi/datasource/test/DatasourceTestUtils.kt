package net.cacheux.dummyapi.datasource.test

import net.cacheux.dummyapi.common.model.DetailedUser
import net.cacheux.dummyapi.common.model.User

/**
 * Utility methods to generate user lists and detailed user for test purposes
 */

fun generateUserList(page: Int, size: Int) =
    (page * size until (page + 1) * size).map {
        User(
            index = it,
            id = "id$it",
            title = "Title$it",
            firstName = "FirstName$it",
            lastName = "LastName$it",
            pictureUrl = "https://randomuser.me/api/portraits/med/men/${it%10}.jpg"
        )
    }

fun generateDetailedUser(uid: String) =
    DetailedUser(
        id = uid,
        title = "Mr",
        firstName = "Georges",
        lastName = "Abitbol",
        pictureUrl = "https://randomuser.me/api/portraits/med/men/1.jpg",
        gender = "Male",
        email = "georges@abitbol.com",
        dateOfBirth = "1956-04-30T19:26:49.610Z",
        phone = "92694011",
        street = "Rue de Rivoli",
        city = "Paris",
        state = "IDF",
        country = "France",
        timezone = "+1:00",
        registerDate = "2021-06-21T21:02:07.374Z",
        updatedDate = "2021-06-21T21:02:07.374Z"
    )