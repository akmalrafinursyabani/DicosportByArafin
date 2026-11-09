package com.example.dicosportbyarafin.presentation.model

import android.os.Parcelable
import com.example.core.domain.model.NewsItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsUiModel(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val badgeUrl: String,
    val stadium: String,
    val formedYear: String,
    val league: String,
    val isFavourite: Boolean
) : Parcelable

fun NewsItem.toUiModel(): NewsUiModel = NewsUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    badgeUrl = badgeUrl,
    stadium = stadium,
    formedYear = formedYear,
    league = league,
    isFavourite = isFavourite
)

fun NewsUiModel.toDomainModel(): NewsItem = NewsItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    badgeUrl = badgeUrl,
    stadium = stadium,
    formedYear = formedYear,
    league = league,
    isFavourite = isFavourite
)
