package com.appttude.h_mal.atlas_weather.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFullWeather(item: EntityItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertListOfFullWeather(items: List<EntityItem>)

    @Query("SELECT * FROM EntityItem WHERE id = :userId LIMIT 1")
    fun getCurrentFullWeather(userId: String) : LiveData<EntityItem>

    @Query("SELECT * FROM EntityItem WHERE id = :userId LIMIT 1")
    suspend fun getCurrentFullWeatherSingle(userId: String) : EntityItem

    @Query("SELECT * FROM EntityItem WHERE id != :id")
    fun getAllFullWeatherWithoutCurrent(id: String = CURRENT_LOCATION) : LiveData<List<EntityItem>>

    @Query("SELECT * FROM EntityItem WHERE id != :id")
    suspend fun getWeatherListWithoutCurrent(id: String = CURRENT_LOCATION) : List<EntityItem>

    @Query("DELETE FROM EntityItem WHERE id = :userId")
    suspend fun deleteEntry(userId: String): Int

}