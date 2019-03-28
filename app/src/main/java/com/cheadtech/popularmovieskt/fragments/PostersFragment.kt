package com.cheadtech.popularmovieskt.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.activities.DetailActivity
import com.cheadtech.popularmovieskt.adapters.PostersAdapter
import com.cheadtech.popularmovieskt.models.Movie
import com.cheadtech.popularmovieskt.network.NetworkUtils
import com.cheadtech.popularmovieskt.room.DatabaseLoader
import com.cheadtech.popularmovieskt.viewmodels.PostersViewModel
import kotlinx.android.synthetic.main.fragment_posters.*

class PostersFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val numCols = 2

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    private val viewModel: PostersViewModel by lazy {
        ViewModelProviders.of(this).get(PostersViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_posters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postersRV?.also { posters ->
            posters.layoutManager = GridLayoutManager(view.context, numCols)
            DisplayMetrics().also { metrics ->
                requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
                val posterPathBase = NetworkUtils.buildPosterUrlBase(metrics.widthPixels / numCols)
                posters.adapter = PostersAdapter(ArrayList(), posterPathBase) { movie ->
                    // This code gets run when a movie is clicked
                    Intent(requireContext(), DetailActivity::class.java).also { intent ->
                        intent.putExtra(getString(R.string.extra_movie), movie)
                        intent.putExtra(getString(R.string.extra_poster_url), posterPathBase + movie.poster_path)
                        startActivity(intent)
                    }
                }
            }
        }

        viewModel.moviesLiveData.observe(this, Observer { updateMoviesAdapter(it) })

        // TODO - look into changing getAllFavoritesLive so it returns an RX observable to be observed in the viewModel instead of the fragment
        DatabaseLoader.getDbInstance(requireContext())
            ?.popularMoviesDao()?.getAllFavoritesLive()?.observe(this, Observer {
            it ?: return@Observer

            if (sharedPreferences.contains(getString(R.string.pref_sort_by_key))) {
                val sort = sharedPreferences.getString(
                    getString(R.string.pref_sort_by_key),
                    getString(R.string.pref_sort_by_popular_value)
                )
                if (sort != null && sort == getString(R.string.pref_sort_by_favorites_value)) {
                    viewModel.sortByFavorites(it)
                }
            }
        })

        setupSharedPreferences()
    }

    private fun setupSharedPreferences() {
        viewModel.init(
            sharedPreferences.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_popular_value)) ?: return,
            DatabaseLoader.getDbInstance(context ?: return) ?: return,
            object : PostersViewModel.PostersViewModelCallback {
                override fun onNetworkError() {
                    Toast.makeText(requireContext(), getString(R.string.alert_network_error), Toast.LENGTH_LONG).show()
                }

                override fun onEmptyFavorites() {
                    // This toast had to be surrounded with a post() to avoid triggering a runtime error when the app initially loads.
                    // The Toast may have been firing off before the fragment was fully loaded and displayed, and was causing a thread error.
                    postersRV?.post {
                        Toast.makeText(requireContext(), getString(R.string.error_no_favorites_found), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onDBError() {
                    Toast.makeText(requireContext(), getString(R.string.error_database), Toast.LENGTH_LONG).show()
                }
            }
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun updateMoviesAdapter(movies: ArrayList<Movie>?) {
        postersRV?.also { posters ->
            (posters.adapter as PostersAdapter?)?.also { it.setData(movies ?: ArrayList()) }
        }
    }

    // SharedPreferences.OnSharedPreferenceChangeListener function
    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.pref_sort_by_key))
            viewModel.refreshMovieList(preferences?.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_popular_value)))
    }
}