package com.msy.themoviemanagerpro.api

import com.msy.themoviemanagerpro.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("search/movie?api_key=58c53d2459b6de7679aa152efc9a6ca8&")
    suspend fun movieSearch(@Query("query") query: String, @Query("language") language: String): Response<MovieResponse>

}