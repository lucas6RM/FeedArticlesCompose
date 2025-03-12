package com.mercierlucas.feedarticlescompose.data.network.dtos


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleDto(
    @Json(name = "categorie")
    val category: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "descriptif")
    val description: String,
    @Json(name = "id")
    val id: Long,
    @Json(name = "id_u")
    val idU: Long,
    @Json(name = "titre")
    val title: String,
    @Json(name = "url_image")
    val urlImage: String
) : Parcelable