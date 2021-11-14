package net.cacheux.dummyapi.datasource.server.json

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var timezone: String? = null
)
