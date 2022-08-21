package com.udacity.asteroidradar.workManager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.local.AsteroidDatabase
import com.udacity.asteroidradar.repo.AsteroidRepository
import retrofit2.HttpException

class DeletePrevDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.deletePreviousDayAsteroids()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }
}