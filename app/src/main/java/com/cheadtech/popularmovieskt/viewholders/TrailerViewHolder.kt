package com.cheadtech.popularmovieskt.viewholders

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.trailer_item.view.*

class TrailerViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
    val trailerLabel: TextView? = itemView.trailer_label
    val layout: LinearLayout? = itemView.trailer_item
}