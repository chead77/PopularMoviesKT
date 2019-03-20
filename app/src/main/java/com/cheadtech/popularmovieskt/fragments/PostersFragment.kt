package com.cheadtech.popularmovieskt.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.activities.DetailActivity
import com.cheadtech.popularmovieskt.adapters.PostersAdapter
import com.cheadtech.popularmovieskt.network.NetworkUtils
import kotlinx.android.synthetic.main.fragment_posters.*

class PostersFragment : Fragment() {
    private val numCols = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_posters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        posters?.also { posters ->
            posters.layoutManager = GridLayoutManager(view.context, numCols)
            DisplayMetrics().also { metrics ->
                requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
                val posterPathBase = NetworkUtils.buildPosterUrlBase(metrics.widthPixels / numCols)
                posters.adapter = PostersAdapter(ArrayList(), posterPathBase) { movie ->
                    // This code gets run when a movie is clicked
                    Intent(requireContext(), DetailActivity::class.java).also { intent ->1
                        intent.putExtra(getString(R.string.extra_movie), movie)
                        intent.putExtra(getString(R.string.extra_poster_url), posterPathBase + movie.poster_path)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}