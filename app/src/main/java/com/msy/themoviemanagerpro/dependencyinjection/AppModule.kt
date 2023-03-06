package com.msy.themoviemanagerpro.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.msy.themoviemanagerpro.R
import com.msy.themoviemanagerpro.api.RetrofitAPI
import com.msy.themoviemanagerpro.database.MovieDao
import com.msy.themoviemanagerpro.database.MovieDatabase
import com.msy.themoviemanagerpro.repo.MovieRepository
import com.msy.themoviemanagerpro.repo.MovieRepositoryInterface
import com.msy.themoviemanagerpro.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room
            .databaseBuilder(
                context,
                MovieDatabase::class.java,
                "movieDB"
            )
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun injectMovieDao(database: MovieDatabase) = database.movieDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI(): RetrofitAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .error(R.mipmap.ic_launcher)
        )

    @Singleton
    @Provides
    fun injectNormalRepo(dao: MovieDao, api: RetrofitAPI) =
        MovieRepository(dao, api) as MovieRepositoryInterface
}