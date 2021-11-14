package net.cacheux.dummyapi.common.model

data class User(
    val index: Int,
    val id: String,
    var title: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var pictureUrl: String? = null
)