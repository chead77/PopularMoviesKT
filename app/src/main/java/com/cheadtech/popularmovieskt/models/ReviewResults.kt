package com.cheadtech.popularmovieskt.models

data class ReviewResults(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: ArrayList<Movie>
)