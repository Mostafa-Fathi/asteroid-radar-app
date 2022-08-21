package com.udacity.asteroidradar.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM databaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM databaseAsteroid WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int
}