package com.cheadtech.popularmovieskt.adapters

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cheadtech.popularmovieskt.R
import com.cheadtech.popularmovieskt.models.Trailer
import com.cheadtech.popularmovieskt.viewholders.TrailerViewHolder
import org.jetbrains.anko.sdk25.coroutines.onClick

class TrailersAdapter(
    private val dataSet: ArrayList<Trailer> = ArrayList()
) : RecyclerView.Adapter<TrailerViewHolder>() {
    fun setData(data: ArrayList<Trailer>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TrailerViewHolder =
        TrailerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.trailer_item, parent, false))

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.trailerLabel?.text = holder.layout?.resources?.getString(R.string.trailer_label, adapterPosition) ?: ""
        holder.layout?.onClick {
            val url = "https://www.youtube.com/watch"
            holder.layout.context.startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url).buildUpon().appendQueryParameter("v", dataSet[adapterPosition].key).build()
            ))
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
