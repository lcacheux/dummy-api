package net.cacheux.dummyapi.datasource.server.json

import kotlinx.serialization.Serializable

@Serializable
data class JsonDetailedUser(
    val id: String = "",
    var title: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var picture: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var dateOfBirth: String? = null,
    var phone: String? = null,
    var location: Location? = null,
    var registerDate: String? = null,
    var updatedDate: String? = null
)