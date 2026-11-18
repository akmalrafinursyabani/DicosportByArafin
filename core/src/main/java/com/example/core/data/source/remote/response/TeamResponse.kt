package com.example.core.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamsResponse(
    @Json(name = "teams")
    val teams: List<TeamDto>?
)

@JsonClass(generateAdapter = true)
data class TeamDto(
    @Json(name = "idTeam")
    val idTeam: String?,
    @Json(name = "strTeam")
    val strTeam: String?,
    @Json(name = "strDescriptionEN")
    val strDescriptionEN: String?,
    @Json(name = "strDescriptionCN")
    val strDescriptionCN: String?,
    @Json(name = "strDescriptionFR")
    val strDescriptionFR: String?,
    @Json(name = "strDescriptionES")
    val strDescriptionES: String?,
    @Json(name = "strDescriptionDE")
    val strDescriptionDE: String?,
    @Json(name = "strDescriptionIT")
    val strDescriptionIT: String?,
    @Json(name = "strDescriptionJP")
    val strDescriptionJP: String?,
    @Json(name = "strDescriptionRU")
    val strDescriptionRU: String?,
    @Json(name = "strDescriptionPT")
    val strDescriptionPT: String?,
    @Json(name = "strDescriptionNL")
    val strDescriptionNL: String?,
    @Json(name = "strDescriptionPL")
    val strDescriptionPL: String?,
    @Json(name = "strBadge")
    val strBadge: String?,
    @Json(name = "strLogo")
    val strLogo: String?,
    @Json(name = "strBanner")
    val strBanner: String?,
    @Json(name = "strFanart1")
    val strFanart1: String?,
    @Json(name = "strStadium")
    val strStadium: String?,
    @Json(name = "intFormedYear")
    val intFormedYear: String?,
    @Json(name = "strLeague")
    val strLeague: String?
)
