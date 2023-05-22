package com.example.travelmemories.memories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemoryDAO {
    @Query("SELECT * FROM memories_table")
    fun getAllMemories(): List<Memory>

    @Query("SELECT * FROM memories_table WHERE _id = :id")
    fun getMemory(id: Int): Memory

    @Insert
    fun insertMemory(memory: Memory)

    @Update
    fun updateMemory(memory: Memory)
}