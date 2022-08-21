package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    fun getAsteroidsListAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<ResponseBody>

    @GET("planetary/apod")
    fun getPictureOfDayAsync(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Deferred<PictureOfDay>
}