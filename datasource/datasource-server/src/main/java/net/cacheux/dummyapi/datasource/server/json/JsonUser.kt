package net.cacheux.dummyapi.datasource.server.json

import kotlinx.serialization.Serializable

@Serializable
data class JsonUser(
    val id: String = "",
    var title: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var picture: String? = null
)