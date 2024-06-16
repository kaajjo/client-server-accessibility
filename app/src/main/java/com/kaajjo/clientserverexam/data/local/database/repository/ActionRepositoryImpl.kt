package com.kaajjo.clientserverexam.data.local.database.repository

import com.kaajjo.clientserverexam.data.local.database.dao.ActionDao
import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction
import com.kaajjo.clientserverexam.domain.repository.ActionRepository
import kotlinx.coroutines.flow.Flow

class ActionRepositoryImpl(
    private val actionDao: ActionDao
) : ActionRepository {
    override fun get(): Flow<List<SwipeAction>> = actionDao.get()

    override fun getByIp(ip: String): Flow<List<SwipeAction>> = actionDao.getByIp(ip)

    override suspend fun insert(action: SwipeAction): Long = actionDao.insert(action)

    override suspend fun update(action: SwipeAction) = actionDao.update(action)

    override suspend fun delete(action: SwipeAction) = actionDao.delete(action)
}