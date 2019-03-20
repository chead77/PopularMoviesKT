package com.cheadtech.popularmovieskt.network

object NetworkUtils {
    fun buildPosterUrlBase(width: Int) = "http://image.tmdb.org/t/p/" +
            when {
                width < (92 + 154) / 2 -> "w92"
                width < (154 + 185) / 2 -> "w154"
                width < (185 + 342) / 2 -> "w185"
                width < (342 + 500) / 2 -> "w342"
                width < (500 + 780) / 2 -> "w500"
                width <= 920 -> "w780"
                else -> "original"
            } + "/"
}