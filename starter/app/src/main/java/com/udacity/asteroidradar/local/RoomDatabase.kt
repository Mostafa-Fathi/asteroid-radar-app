package com.udacity.asteroidradar.local

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.flow.Flow

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}