package com.cheadtech.popularmovieskt.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PopularMoviesDao {
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): List<Favorite>

    // TODO - look into changing this so it returns an RX observable to be observed in the viewModel instead of the fragment
    @Query("SELECT * FROM favorite")
    fun getAllFavoritesLive(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE id = :movieId")
    fun getFavorite(movieId: Int?): Favorite

    @Query("SELECT * FROM favorite WHERE id = :movieId")
    fun getLiveFavorite(movieId: Int?): LiveData<Favorite>

    @Insert
    fun insertAll(vararg favorites: Favorite)

    @Delete
    fun delete(favorite: Favorite)
}