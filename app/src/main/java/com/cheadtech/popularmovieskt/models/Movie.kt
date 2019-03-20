package com.cheadtech.popularmovieskt.models

import java.io.Serializable
import java.util.ArrayList

data class Movie(
    var vote_count: Int? = null,
    var id: Int? = null,
    var video: Boolean? = null,
    var vote_average: Double? = null,
    var title: String,
    var popularity: Double? = null,
    var poster_path: String,
    var original_language: String,
    var original_title: String,
    var genre_ids: ArrayList<Int>,
    var backdrop_path: String,
    var adult: Boolean? = null,
    var overview: String,
    var release_date: String
) : Serializable