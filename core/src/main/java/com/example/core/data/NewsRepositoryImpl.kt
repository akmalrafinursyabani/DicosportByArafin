package com.example.core.data

import android.util.Log
import com.example.core.data.source.local.entity.NewsEntity
import com.example.core.data.source.local.room.NewsDao
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.NewsRepository
import com.example.core.domain.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NewsRepositoryImpl(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    private fun sanitizeUrl(url: String?): String {
        if (url.isNullOrBlank()) return ""
        val fixed = if (url.startsWith("http://")) url.replace("http://", "https://") else url
        return fixed
    }

    override fun getEplNews(): Flow<Resource<List<NewsItem>>> = flow {
        emit(Resource.Loading)
        
        // 1. Emit local cache first (Offline-first approach)
        val localData = newsDao.getAllNews().map { entities ->
            entities.map { entity ->
                NewsItem(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    imageUrl = sanitizeUrl(entity.imageUrl),
                    badgeUrl = sanitizeUrl(entity.badgeUrl),
                    stadium = entity.stadium,
                    formedYear = entity.formedYear,
                    league = entity.league,
                    isFavourite = entity.isFavourite
                )
            }
        }

        // Try fetching from network and updating cache
        try {
            val response = apiService.getEplTeams()
            val teamList = response.teams ?: emptyList()
            Log.d("Dicosport", "Fetched ${teamList.size} teams from API")
            if (teamList.isNotEmpty()) {
                val currentFavourites = newsDao.getFavouriteNews().first().map { it.id }.toSet()
                val newsEntities = teamList.map { team ->
                    val banner = team.strBanner ?: team.strFanart1 ?: team.strBadge ?: team.strLogo ?: ""
                    val imageUrlRaw = if (banner.isNotBlank()) {
                        if (!banner.endsWith("/medium")) "$banner/medium" else banner
                    } else {
                        ""
                    }
                    val imageUrl = sanitizeUrl(imageUrlRaw)
                    val badgeUrl = sanitizeUrl(team.strBadge ?: team.strLogo)
                    val id = team.idTeam ?: ""
                    Log.d("Dicosport", "Team: ${team.strTeam}, banner: $banner, imageUrl: $imageUrl")
                    NewsEntity(
                        id = id,
                        title = team.strTeam ?: "Unknown Team",
                        description = team.strDescriptionEN ?: "No description available for this sports news item.",
                        imageUrl = imageUrl,
                        badgeUrl = badgeUrl,
                        stadium = team.strStadium ?: "Unknown Stadium",
                        formedYear = team.intFormedYear ?: "N/A",
                        league = team.strLeague ?: "English Premier League",
                        isFavourite = currentFavourites.contains(id),
                        isCached = true
                    )
                }
                newsDao.insertNews(newsEntities)
            }
        } catch (e: Exception) {
            Log.e("Dicosport", "Error fetching teams from API: ${e.localizedMessage}", e)
            val cached = localData.first()
            if (cached.isEmpty()) {
                emit(Resource.Error(e.localizedMessage ?: "No internet connection and no cached data available."))
                return@flow
            }
        }

        // Emit final result from local database (Single Source of Truth)
        emitAll(localData.map { Resource.Success(it) })
    }

    override fun getFavouriteNews(): Flow<List<NewsItem>> {
        return newsDao.getFavouriteNews().map { entities ->
            entities.map { entity ->
                NewsItem(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    imageUrl = sanitizeUrl(entity.imageUrl),
                    badgeUrl = sanitizeUrl(entity.badgeUrl),
                    stadium = entity.stadium,
                    formedYear = entity.formedYear,
                    league = entity.league,
                    isFavourite = true
                )
            }
        }
    }

    override suspend fun setFavouriteNews(newsItem: NewsItem, state: Boolean) {
        newsDao.updateFavouriteStatus(newsItem.id, state)
        val entity = NewsEntity(
            id = newsItem.id,
            title = newsItem.title,
            description = newsItem.description,
            imageUrl = newsItem.imageUrl,
            badgeUrl = newsItem.badgeUrl,
            stadium = newsItem.stadium,
            formedYear = newsItem.formedYear,
            league = newsItem.league,
            isFavourite = state,
            isCached = true
        )
        try {
            newsDao.updateNews(entity)
        } catch (_: Exception) {
            newsDao.insertNews(listOf(entity))
        }
    }

    override fun isFavouriteNews(id: String): Flow<Boolean> {
        return newsDao.isFavourite(id)
    }
}
