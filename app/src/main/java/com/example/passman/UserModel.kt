package com.example.passman

data class UserModel (
    var Id: String? = null,
    val Username: String? = null,
    val Password: String? = null,
    val SaltString: String? = null,
)
