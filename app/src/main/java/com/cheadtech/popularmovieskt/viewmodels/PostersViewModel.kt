package com.cheadtech.popularmovieskt.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.NonNull
import android.util.Log
import com.cheadtech.popularmovieskt.BuildConfig
import com.cheadtech.popularmovieskt.models.Movie
import com.cheadtech.popularmovieskt.models.MovieResults
import com.cheadtech.popularmovieskt.network.ServiceLocator
import com.cheadtech.popularmovieskt.room.Favorite
import com.cheadtech.popularmovieskt.room.PopularMoviesDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.concurrent.thread

class PostersViewModel : ViewModel() {
    private val tag = javaClass.toString()
    val moviesLiveData: MutableLiveData<ArrayList<Movie>> = MutableLiveData()

    private var db: PopularMoviesDB? = null

    interface PostersViewModelCallback {
        fun onNetworkError()
        fun onEmptyFavorites()
        fun onDBError()
    }
    private var callback: PostersViewModelCallback? = null

    fun init(@NonNull sortBy: String, @NonNull dbInstance: PopularMoviesDB, @NonNull callback: PostersViewModelCallback) {
        this.callback = callback
        db = dbInstance
        refreshMovieList(sortBy)
    }

    fun refreshMovieList(sortBy: String?) {
        when (sortBy) {
            "favorites" ->
                sortByFavorites()
            "popular", "top_rated" -> {
                val service = ServiceLocator.getTMDBService()
                service.getMovies(sortBy, BuildConfig.API_KEY).enqueue(object : Callback<MovieResults> {
                    override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                        when {
                            response.code() == 200 -> response.body()?.also {
                                moviesLiveData.postValue(it.results)
                                return
                            }
                            response.errorBody() != null -> Log.e(tag, response.errorBody()!!.toString())
                            else -> Log.e(tag, " - Network response was unsuccessful.")
                        }
                        callback?.onNetworkError()
                    }

                    override fun onFailure(call: Call<MovieResults>, t: Throwable) {
                        Log.e(tag, " - Network call failed: " + t.message)
                        callback?.onNetworkError()
                    }
                })
            }
        }
    }

    private fun sortByFavorites() {
        thread {
            try {
                db?.popularMoviesDao()?.also {
                    sortByFavorites( it.getAllFavorites()) }
            } catch (e: Exception) {
                Log.e(tag, e.message)
                callback?.onDBError()
            }
        }
    }

    fun sortByFavorites(@NonNull favorites: List<Favorite>) {
        if (favorites.isEmpty()) {
            Log.w(tag, "No favorites selected")
            callback?.onEmptyFavorites()
            return
        }
        ArrayList<Movie>().also { movies ->
            favorites.forEach {
                movies.add(Movie(0, it.id, true, it.voteAverage, it.title, 0.0,
                    it.posterPath, null, it.originalTitle, null,
                    false, it.movieOverview, it.releaseDate))
            }
            moviesLiveData.postValue(movies)
        }
    }
}
