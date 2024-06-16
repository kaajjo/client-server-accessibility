package com.kaajjo.clientserverexam.di

import androidx.core.app.PendingIntentCompat.send
import com.kaajjo.clientserverexam.data.local.database.entity.SwipeAction
import com.kaajjo.clientserverexam.domain.repository.ActionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Named
import javax.inject.Singleton
import kotlin.random.Random

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {

    @Provides
    @Singleton
    fun provideEmbeddedServer(
        @Named("port") port: Int,
        actionRepository: ActionRepository
    ) = embeddedServer(
        Netty,
        port = port
    ) {
        install(WebSockets)
        routing {
            webSocket {
                this.incoming.receiveAsFlow().onEach { frame ->
                    val text = (frame as Frame.Text)

                    val ipAddress = call.request.local.remoteHost
                    val dateTime = LocalDateTime.now()

                    val data = text.readText().split(";")
                    println(data.size)
                    println(data.toString())
                    val action = SwipeAction(
                        id = 0,
                        posX = data[0].toFloat(),
                        posY = data[1].toFloat(),
                        swipeLength = data[2].toFloat(),
                        swipeDown = data[3].toBoolean(),
                        gestureCompleted = data[4].toBoolean(),
                        dateTime = dateTime,
                        clientIp = ipAddress
                    )

                    println(action.toString())
                    withContext(Dispatchers.IO) {
                        actionRepository.insert(action)
                    }

                }.launchIn(this)

                while (true) {
                    val relPos = Pair(
                        Random.nextFloat(),
                        Random.nextFloat(),
                    )
                    val relSwipe = Random.nextFloat()
                    val swipeDown = Random.nextBoolean()
                    send("${relPos.first};${relPos.second};${relSwipe};$swipeDown")
                    delay(1000)
                }
            }
        }
    }
}