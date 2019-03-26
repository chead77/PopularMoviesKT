package com.cheadtech.popularmovieskt.models

import java.io.Serializable
import java.util.ArrayList

data class Movie(
    var vote_count: Int?,
    var id: Int?,
    var video: Boolean?,
    var vote_average: Double?,
    var title: String?,
    var popularity: Double?,
    var poster_path: String?,
    var original_language: String?,
    var original_title: String?,
    var genre_ids: ArrayList<Int>?,
    var backdrop_path: String?,
    var adult: Boolean?,
    var overview: String?,
    var release_date: String?
) : Serializable // TODO change this to Parcelable