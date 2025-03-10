package com.mercierlucas.feedarticlescompose.data.model

import com.squareup.moshi.Json

data class LoginModel(
    val login: String,
    val password: String
)

data class RegisterModel(
    val login: String,
    val password: String,
    val confirmedPassword: String
)