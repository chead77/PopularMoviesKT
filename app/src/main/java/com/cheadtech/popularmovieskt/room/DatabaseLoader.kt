package com.cheadtech.popularmovieskt.room

import android.arch.persistence.room.Room
import android.content.Context
import com.cheadtech.popularmovieskt.R

object DatabaseLoader {
    private var dbInstance: PopularMoviesDB? = null
    fun getDbInstance(context: Context): PopularMoviesDB? =
        dbInstance ?: run {
            dbInstance = Room.databaseBuilder(context, PopularMoviesDB::class.java, context.getString(R.string.app_db_name)).build()
            dbInstance
        }
}
