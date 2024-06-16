package com.kaajjo.clientserverexam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.kaajjo.clientserverexam.data.local.database.converter.LocalDateTimeConverter
import com.kaajjo.clientserverexam.data.local.database.dao.ActionDao
import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction

@Database(
    entities = [ SwipeAction::class ],
    version = 1
)
@TypeConverters(
    LocalDateTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
            }

            return INSTANCE as AppDatabase
        }
    }
}