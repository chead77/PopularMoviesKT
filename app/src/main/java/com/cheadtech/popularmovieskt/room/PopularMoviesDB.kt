package com.cheadtech.popularmovieskt.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class PopularMoviesDB : RoomDatabase(){
    abstract fun popularMoviesDao(): PopularMoviesDao
}
