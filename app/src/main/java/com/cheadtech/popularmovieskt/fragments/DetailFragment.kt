package com.cheadtech.popularmovieskt.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.adapters.ReviewsAdapter
import com.cheadtech.popularmovieskt.adapters.TrailersAdapter
import com.cheadtech.popularmovieskt.models.Movie
import com.cheadtech.popularmovieskt.room.DatabaseLoader
import com.cheadtech.popularmovieskt.room.Favorite
import com.cheadtech.popularmovieskt.viewmodels.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class DetailFragment : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(DetailViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        layoutInflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie: Movie? = activity?.intent?.getParcelableExtra(getString(R.string.extra_movie))
        movie ?: run {
            Log.e(this::class.java.toString(), "Movie extra could not be found")
            return
        }

        toolbar?.title = movie.title

        var posterUrl = ""
        activity?.intent?.also {
            if (it.hasExtra(getString(R.string.extra_poster_url)))
                posterUrl = it.getStringExtra(getString(R.string.extra_poster_url))
            else
                Log.e(this::class.java.toString(), "poster URL not provided in fragment extras")
            Picasso.get().load(posterUrl)
                .placeholder(R.drawable.ic_hourglass_empty_black)
                .error(R.drawable.ic_error_outline_black)
                .into(poster_thumbnail)
        }

        synopsis?.text = movie.overview
        user_rating?.text = getString(R.string.user_rating_formatted, movie.vote_average.toString())
        release_date?.text = movie.release_date?.substring(0, 4) ?: ""
        favorite?.onClick { viewModel.onFavoriteClicked(movie) }

        // RecyclerView adapters and observers
        trailersRV?.also {
            it.adapter = TrailersAdapter()
            it.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            viewModel.trailersLD.observe(this, Observer { trailers ->
                trailers ?: return@Observer

                (trailersRV?.adapter as TrailersAdapter?)?.also { adapter ->
                    adapter.setData(trailers)
                    if (trailers.size == 0) {
                        trailers_empty_message?.visibility = View.VISIBLE
                        trailersRV.visibility = View.GONE
                    } else run {
                        trailers_empty_message.visibility = View.GONE
                        trailersRV.visibility = View.VISIBLE
                    }
                }
            })
        }
        reviewsRV?.also {
            it.adapter = ReviewsAdapter()
            it.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            viewModel.reviewsLD.observe(this, Observer { reviews ->
                reviews ?: return@Observer

                (reviewsRV?.adapter as ReviewsAdapter?)?.also { adapter ->
                    adapter.setData(reviews)
                    if (reviews.size == 0) {
                        reviews_empty_message.visibility = View.VISIBLE
                        reviewsRV.visibility = View.GONE
                    } else run {
                        reviews_empty_message.visibility = View.GONE
                        reviewsRV.visibility = View.VISIBLE
                    }
                }
            })
        }

        DatabaseLoader.getDbInstance(activity?.applicationContext ?: return)?.also { db ->
            viewModel.init(movie, db, object : DetailViewModel.DetailViewModelCallback {
                override fun onNetworkError(messageResourceStringId: Int) {
                    Toast.makeText(context, getString(messageResourceStringId), Toast.LENGTH_LONG).show()
                }
            })
            db.popularMoviesDao().getLiveFavorite(movie.id).observe(this, Observer<Favorite> {
                setFavoriteButtonState(it != null) })
        }
    }

    private fun setFavoriteButtonState(fav: Boolean) {
        activity?.also {
            if (fav)
                it.favorite?.setImageDrawable(it.getDrawable(android.R.drawable.star_big_on))
            else
                it.favorite?.setImageDrawable(it.getDrawable(android.R.drawable.star_big_off))
        }
    }
}
