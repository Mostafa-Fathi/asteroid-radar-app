@file:Suppress("unused")

package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.utils.Constants.DELETE_ASTEROIDS_WORK_NAME
import com.udacity.asteroidradar.utils.Constants.REFRESH_ASTEROIDS_WORK_NAME
import com.udacity.asteroidradar.workManager.DeletePrevDataWorker
import com.udacity.asteroidradar.workManager.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            setupWorkers()
        }
    }

    private fun setupWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1, TimeUnit.DAYS).setConstraints(constraints).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            REFRESH_ASTEROIDS_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRefreshRequest)

        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<DeletePrevDataWorker>(
            1, TimeUnit.DAYS).setConstraints(constraints).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DELETE_ASTEROIDS_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingDeleteRequest)
    }
}