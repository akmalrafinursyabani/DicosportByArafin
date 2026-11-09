package com.example.core.domain.model

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val badgeUrl: String,
    val stadium: String,
    val formedYear: String,
    val league: String,
    var isFavourite: Boolean = false
)
