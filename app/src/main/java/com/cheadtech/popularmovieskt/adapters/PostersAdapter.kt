package com.cheadtech.popularmovieskt.adapters

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.models.Movie
import com.cheadtech.popularmovieskt.viewholders.PosterViewHolder
import com.squareup.picasso.Picasso

class PostersAdapter(
    @NonNull private val dataSet: ArrayList<Movie>,
    @NonNull private var posterUrlBase: String,
    @NonNull private val callback: (Movie) -> Unit
) : RecyclerView.Adapter<PosterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PosterViewHolder =
        PosterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.poster_item, parent, false))

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        Picasso.get().load(posterUrlBase + dataSet[position].poster_path)
            .placeholder(R.drawable.ic_hourglass_empty_black)
            .error(R.drawable.ic_error_outline_black)
            .into(holder.poster)
        holder.poster.contentDescription = holder.poster.context.getString(R.string.movie_poster_content_description, dataSet[position].title)
        holder.poster.setOnClickListener { callback.invoke(dataSet[position]) }
    }

    fun setData(data: ArrayList<Movie>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}
