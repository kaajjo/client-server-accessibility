package com.kaajjo.clientserverexam.domain.repository

import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction
import kotlinx.coroutines.flow.Flow

interface ActionRepository {
    fun get(): Flow<List<SwipeAction>>

    fun getByIp(ip: String): Flow<List<SwipeAction>>

    suspend fun insert(action: SwipeAction): Long

    suspend fun update(action: SwipeAction)

    suspend fun delete(action: SwipeAction)
}