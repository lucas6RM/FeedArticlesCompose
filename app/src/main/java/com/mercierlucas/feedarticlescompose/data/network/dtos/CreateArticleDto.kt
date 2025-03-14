package com.mercierlucas.feedarticlescompose.data.network.dtos



import com.squareup.moshi.Json

data class CreateArticleDto(
    @Json(name = "cat")
    val cat: Int,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "id_u")
    val idU: Long,
    @Json(name = "image")
    val image: String,
    @Json(name = "title")
    val title: String
)