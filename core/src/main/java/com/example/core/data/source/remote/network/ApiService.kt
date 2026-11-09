package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search_all_teams.php")
    suspend fun getEplTeams(
        @Query("l") league: String = "English_Premier_League"
    ): TeamsResponse
}
