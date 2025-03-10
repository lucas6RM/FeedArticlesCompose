package com.mercierlucas.feedarticlescompose.data.network.dtos


import com.mercierlucas.feedarticlescompose.data.model.Article
import com.squareup.moshi.Json

data class GetAllArticlesDto(
    @Json(name = "articles")
    val articles: List<Article>,
)