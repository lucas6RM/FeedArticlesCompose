package com.mercierlucas.feedarticlescompose.data.model


import com.squareup.moshi.Json

data class Article(
    @Json(name = "categorie")
    val categorie: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "descriptif")
    val descriptif: String,
    @Json(name = "id")
    val id: Long,
    @Json(name = "id_u")
    val idU: Long,
    @Json(name = "titre")
    val titre: String,
    @Json(name = "url_image")
    val urlImage: String
)