package com.cheadtech.popularmovieskt.network

import com.cheadtech.popularmovieskt.models.MovieResults
import com.cheadtech.popularmovieskt.models.ReviewResults
import com.cheadtech.popularmovieskt.models.TrailerResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {
    @GET("3/movie/{sort_by}")
    fun getMovies(@Path("sort_by") sortBy: String, @Query("api_key") apiKey: String): Call<MovieResults>

    @GET("3/movie/{id}/videos")
    fun getTrailers(@Path("sort_by") sortBy: String, @Query("api_key") apiKey: String): Call<TrailerResults>

    @GET("3/movie/{sort_by}")
    fun getReviews(@Path("sort_by") sortBy: String, @Query("api_key") apiKey: String): Call<ReviewResults>
}