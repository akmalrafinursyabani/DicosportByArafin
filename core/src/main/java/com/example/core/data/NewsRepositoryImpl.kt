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

    private fun getLocalizedDescription(team: com.example.core.data.source.remote.response.TeamDto): String {
        val appLocales = androidx.appcompat.app.AppCompatDelegate.getApplicationLocales()
        val locale = if (!appLocales.isEmpty) appLocales.get(0) else java.util.Locale.getDefault()
        val language = locale?.language?.lowercase() ?: "en"

        val desc = when (language) {
            "zh" -> team.strDescriptionCN
            "fr" -> team.strDescriptionFR
            "es" -> team.strDescriptionES
            "de" -> team.strDescriptionDE
            "it" -> team.strDescriptionIT
            "ja" -> team.strDescriptionJP
            "ru" -> team.strDescriptionRU
            "pt" -> team.strDescriptionPT
            "nl" -> team.strDescriptionNL
            "pl" -> team.strDescriptionPL
            else -> team.strDescriptionEN
        }

        return if (!desc.isNullOrBlank()) desc else (team.strDescriptionEN ?: "No description available for this sports news item.")
    }

    override fun getEplNews(): Flow<Resource<List<NewsItem>>> = flow {
        emit(Resource.Loading)

        try {
            val response = apiService.getEplTeams()
            val teamList = response.teams ?: emptyList()
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
                    val description = getLocalizedDescription(team)
                    NewsEntity(
                        id = id,
                        title = team.strTeam ?: "Unknown Team",
                        description = description,
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
        }

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

        val firstCached = localData.first()
        if (firstCached.isEmpty()) {
            emit(Resource.Error("No internet connection and no cached data available."))
        } else {
            emitAll(localData.map { Resource.Success(it) })
        }
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
        newsDao.insertNews(listOf(entity))
        newsDao.updateFavouriteStatus(newsItem.id, state)
    }

    override fun isFavouriteNews(id: String): Flow<Boolean> {
        return newsDao.isFavourite(id)
    }
}
