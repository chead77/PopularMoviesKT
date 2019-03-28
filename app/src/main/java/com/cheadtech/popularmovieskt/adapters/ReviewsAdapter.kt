package com.cheadtech.popularmovieskt.adapters

import android.content.Intent
import android.net.Uri
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.models.Review
import com.cheadtech.popularmovieskt.viewholders.ReviewViewHolder
import org.jetbrains.anko.sdk25.coroutines.onClick

class ReviewsAdapter(@NonNull private val dataSet: ArrayList<Review> = ArrayList()) : RecyclerView.Adapter<ReviewViewHolder>() {
    fun setData(data: ArrayList<Review>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ReviewViewHolder =
        ReviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false))

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.author?.text = holder.author?.context?.getString(R.string.written_by, dataSet[adapterPosition].author) ?: ""
        holder.content?.text = dataSet[adapterPosition].content
        holder.layout?.onClick {
            holder.layout.context.startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(dataSet[adapterPosition].url)
            ))
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
