package com.msy.themoviemanagerpro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchListMovie(movie: WatchListMovie)

    @Query("DELETE FROM favoriteMovies WHERE `api_id` = :api_id")
    suspend fun deleteFavoriteMovie(api_id: Int)

    @Query("DELETE FROM watchListMovies WHERE `api_id` = :api_id")
    suspend fun deleteWatchListMovie(api_id: Int)

    @Query("SELECT * FROM favoriteMovies")
    fun observeFavoriteMovies(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM watchListMovies")
    fun observeWatchListMovies(): LiveData<List<WatchListMovie>>

    @Query("SELECT EXISTS (SELECT 1 FROM favoriteMovies WHERE `api_id`= :api_id)")
    suspend fun checkIsInFavorites(api_id: Int) : Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM watchListMovies WHERE `api_id`= :api_id)")
    suspend fun checkIsInWatchList(api_id: Int) : Boolean


}