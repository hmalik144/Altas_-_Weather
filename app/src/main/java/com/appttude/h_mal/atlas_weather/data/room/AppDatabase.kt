package com.appttude.h_mal.atlas_weather.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem

@Database(
    entities = [EntityItem::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        // create an instance of room database or use previously created instance
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            ).addTypeConverter(Converter(context))
                .addMigrations(Migration(1,2) { })
                .build()
    }
}

