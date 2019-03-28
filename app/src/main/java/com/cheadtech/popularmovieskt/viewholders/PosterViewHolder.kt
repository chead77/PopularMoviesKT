package com.cheadtech.popularmovieskt.viewholders

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.poster_item.view.*

class PosterViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    val poster: ImageView = itemView.poster
}