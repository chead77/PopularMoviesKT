package com.cheadtech.popularmovieskt.models

data class ReviewResults(
    val id: Int,
    val page: Int,
    val results: ArrayList<Review>,
    val total_pages: Int,
    val total_results: Int
)