package net.cacheux.dummyapi.datasource.server.json

import kotlinx.serialization.*

@Serializable
data class JsonResult<T>(
    var data: List<T>? = null,
    var total: Int? = null,
    var page: Int? = null,
    var limit: Int? = null
)