package com.cheadtech.popularmovieskt.viewholders

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    val author: TextView? = itemView.author
    val content: TextView? = itemView.content
    val layout: LinearLayout? = itemView.review_item
}