package com.mercierlucas.feedarticlescompose.data.model


data class CreateArticleModel(
    val title: String,
    val desc: String,
    val cat: Int,
    val imageUrl : String,
)