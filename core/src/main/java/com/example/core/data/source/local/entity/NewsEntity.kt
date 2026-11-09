package com.example.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "badgeUrl")
    val badgeUrl: String,

    @ColumnInfo(name = "stadium")
    val stadium: String,

    @ColumnInfo(name = "formedYear")
    val formedYear: String,

    @ColumnInfo(name = "league")
    val league: String,

    @ColumnInfo(name = "isFavourite")
    val isFavourite: Boolean = false,

    @ColumnInfo(name = "isCached")
    val isCached: Boolean = true
)
