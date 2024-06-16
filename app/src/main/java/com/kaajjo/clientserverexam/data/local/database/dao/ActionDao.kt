package com.kaajjo.clientserverexam.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Query("SELECT * FROM SwipeAction ORDER BY id DESC")
    fun get(): Flow<List<SwipeAction>>

    @Query("SELECT * FROM SwipeAction WHERE client_ip == :ip")
    fun getByIp(ip: String): Flow<List<SwipeAction>>

    @Insert
    suspend fun insert(action: SwipeAction): Long

    @Update
    suspend fun update(action: SwipeAction)

    @Delete
    suspend fun delete(action: SwipeAction)
}