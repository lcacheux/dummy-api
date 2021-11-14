package net.cacheux.dummyapi.common.model

data class DetailedUser(
    val id: String,
    val title: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val pictureUrl: String? = null,

    // Start of detailed fields
    val gender: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val phone: String? = null,

    // Location infos
    val street: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val timezone: String? = null,

    val registerDate: String? = null,
    val updatedDate: String? = null
)
