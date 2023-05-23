package com.example.travelmemories.memories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memory::class], version = 1)
abstract class MemoryDatabase: RoomDatabase() {
    abstract fun memoryDao(): MemoryDAO

    companion object {
        private const val DATABASE_NAME = "memories_database"

        @Volatile
        private var INSTANCE: MemoryDatabase? = null

        fun getInstance(context: Context): MemoryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoryDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}