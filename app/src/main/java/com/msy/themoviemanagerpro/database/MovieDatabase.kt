package com.msy.themoviemanagerpro.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class, WatchListMovie::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}