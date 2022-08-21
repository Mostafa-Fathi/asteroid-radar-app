package com.udacity.asteroidradar.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.api.RetrofitHelper.service
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.local.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidRepository (private val database: AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun refreshAsteroids(
        startDate: String = getToday(), endDate: String = getSeventhDay())
    {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = service.getAsteroidsListAsync(
                startDate, endDate, Constants.API_KEY).await()

            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
           // Log.e("asteroidList", asteroidList.toString())
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousDayAsteroids(getToday())
        }
    }
}