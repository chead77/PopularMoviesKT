package com.cheadtech.popularmovieskt.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.cheadtech.popularmovieskt.BuildConfig
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.models.*
import com.cheadtech.popularmovieskt.network.ServiceLocator
import com.cheadtech.popularmovieskt.room.Favorite
import com.cheadtech.popularmovieskt.room.PopularMoviesDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class DetailViewModel : ViewModel() {
    private var db: PopularMoviesDB? = null

    val trailersLD: MutableLiveData<ArrayList<Trailer>> = MutableLiveData()
    val reviewsLD: MutableLiveData<ArrayList<Review>> = MutableLiveData()

    interface DetailViewModelCallback {
        fun onNetworkError(messageResourceStringId: Int)
    }
    private var callback: DetailViewModelCallback? = null

    fun init(movie: Movie, db: PopularMoviesDB, callback: DetailViewModelCallback) {
        this.db = db
        this.callback = callback
        getTrailerList(movie)
        getReviewList(movie)
    }

    private fun getTrailerList(movie: Movie) {
        ServiceLocator.getTMDBService().also { tmdbService ->
            tmdbService.getTrailers(movie.id ?: 0, BuildConfig.API_KEY).enqueue(object : Callback<TrailerResults> {
                override fun onResponse(call: Call<TrailerResults>, response: Response<TrailerResults>) {
                    when {
                        response.code() == 200 -> response.body()?.results?.also {
                            trailersLD.postValue(it)
                            return
                        } ?: run {
                            Log.e(this@DetailViewModel::class.java.toString(), " - results not found in network response")
                        }
                        response.errorBody() != null -> Log.e(this@DetailViewModel.javaClass.toString(), response.errorBody()!!.toString())
                        else -> Log.e(this@DetailViewModel::class.java.toString(), " - Network response was unsuccessful.")
                    }
                    callback?.onNetworkError(R.string.alert_network_error_trailers)
                }

                override fun onFailure(call: Call<TrailerResults>, t: Throwable) {
                    Log.e(this@DetailViewModel.javaClass.toString(), " - Network call failed: " + t.message)
                    callback?.onNetworkError(R.string.alert_network_error_trailers)
                }
            })
        }
    }

    private fun getReviewList(movie: Movie) {
        ServiceLocator.getTMDBService().also { tmdbService ->
            tmdbService.getReviews(movie.id ?: 0, BuildConfig.API_KEY).enqueue(object : Callback<ReviewResults> {
                override fun onResponse(call: Call<ReviewResults>, response: Response<ReviewResults>) {
                    when {
                        response.code() == 200 -> response.body()?.results?.also {
                            reviewsLD.postValue(it)
                            return
                        } ?: run {
                            Log.e(this@DetailViewModel::class.java.toString(), " - results not found in network response")
                        }
                        response.errorBody() != null -> Log.e(this@DetailViewModel.javaClass.toString(), response.errorBody()!!.toString())
                        else -> Log.e(this@DetailViewModel::class.java.toString(), " - Network response was unsuccessful.")
                    }
                    callback?.onNetworkError(R.string.alert_network_error_reviews)
                }

                override fun onFailure(call: Call<ReviewResults>, t: Throwable) {
                    Log.e(this@DetailViewModel.javaClass.toString(), " - Network call failed: " + t.message)
                    callback?.onNetworkError(R.string.alert_network_error_reviews)
                }
            })
        }
    }

    fun onFavoriteClicked(movie: Movie) {
        thread {
            try {
                val favorite = db?.popularMoviesDao()?.getFavorite(movie.id)
                if (favorite != null)
                    db?.popularMoviesDao()?.delete(favorite)
                else {
                    Favorite().also {
                        it.id = movie.id
                        it.title = movie.title
                        it.originalTitle = movie.original_title
                        it.posterPath = movie.poster_path
                        it.releaseDate = movie.release_date
                        it.voteAverage = movie.vote_average
                        it.movieOverview = movie.overview
                        db?.popularMoviesDao()?.insertAll(it)
                    }
                }
            } catch (e: Exception) {
                Log.e(this@DetailViewModel.javaClass.toString(), e.message)
            }

        }
    }
}
