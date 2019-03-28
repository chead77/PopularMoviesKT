package com.cheadtech.popularmovieskt.network

object NetworkUtils {
    fun buildPosterUrlBase(width: Int) = "http://image.tmdb.org/t/p/" +
            /*
            Sets up the poster image URL prefix, plus the path of the poster size needed. Since the grid is 2 columns wide
            with no margins or padding, find the size of the screen and divide by 2, then compare that size to the average
            between two consecutive remote image sizes to determine which size to use in the image URL. This URL path will
            be concatenated with the poster image name to produce the full url string for the poster image. The thumbnail
            on the detail screen will also use this path
             */
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