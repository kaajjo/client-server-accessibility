package com.kaajjo.client.domain.service

import com.kaajjo.client.domain.model.SwipeAction
import kotlinx.coroutines.flow.Flow

/**
 * Client socket service to interact with the server through WebSocket
 */
interface ClientSocketService {

    /**
     * Init new WebSocket session with IP and Port
     *
     * @param ip IP Address of the server to connect
     * @param port Port of the server to connect
     * @return
     */
    suspend fun initSession(ip: String, port: Int): Boolean

    /**
     * Observe actions from WebSocket session as flow
     *
     * @return Flow of SwipeActions
     */
    fun observeActions(): Flow<SwipeAction>

    /**
     * Send status to the server
     *
     * @param status message to send
     */
    suspend fun sendStatus(status: String)

    /**
     * Close WebSocket session
     *
     */
    suspend fun close()
}