package com.mercierlucas.feedarticlescompose.data.network.dtos


import com.squareup.moshi.Json

data class ReturnLoginDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "token")
    val token: String?
)