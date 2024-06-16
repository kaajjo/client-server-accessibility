package com.kaajjo.client.data.remote.service

import android.util.Log
import com.kaajjo.client.domain.model.SwipeAction
import com.kaajjo.client.domain.service.ClientSocketService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

class ClientSocketServiceImpl(
    private val client: HttpClient
) : ClientSocketService {

    private var session: WebSocketSession? = null

    override suspend fun initSession(ip: String, port: Int): Boolean {
        return try {
            session = client.webSocketSession(
                host = ip,
                port = port
            )

            session?.isActive == true
        } catch (e: Exception) {
            Log.e("initSession", e.message.toString())
            false
        }
    }

    override fun observeActions(): Flow<SwipeAction> {
        return try {
            session
                ?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val data = (it as Frame.Text).readText().split(";")

                    SwipeAction(
                        relativePos = Pair(data[0].toFloat(), data[1].toFloat()),
                        relativeSwipe = data[2].toFloat(),
                        swipeDown = data[3].toBoolean()
                    )
                } ?: flow { }
        } catch (e: Exception) {
            Log.e("observeAction", e.message.toString())
            flow { }
        }
    }

    override suspend fun sendStatus(status: String) {
        try {
            session?.send(status)
        } catch (e: Exception) {
            Log.e("sendStatus", e.message.toString())
        }
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Closed by user"))
    }
}