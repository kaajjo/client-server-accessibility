package com.kaajjo.client.system.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import com.kaajjo.client.data.local.datastore.PreferencesDataStore
import com.kaajjo.client.domain.service.ClientSocketService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val MIN_HEIGHT = 200f

@AndroidEntryPoint
class AccessibilityService : AccessibilityService() {
    @Inject
    lateinit var client: ClientSocketService

    @Inject
    lateinit var dataStore: PreferencesDataStore

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    override fun onInterrupt() {
        try {
            scope.launch {
                client.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            if (intent?.action == ACTION_DISABLE_SERVICE) {
                disableSelf()
            }

            scope.launch {
                val ip = runBlocking { dataStore.serverIp.first() }
                val port = runBlocking { dataStore.serverPort.first() }

                val result = client.initSession(ip, port)
                if (!result) return@launch

                client.observeActions()
                    .onEach { action ->
                        val width = resources.displayMetrics.widthPixels
                        val height = resources.displayMetrics.heightPixels

                        // calculate real position on the screen
                        val absolutePos = Pair(
                            action.relativePos.second * width,
                            action.relativePos.first * height,
                        )

                        // calculate real swipe height (*0.5 to make then not so big)
                        var swipeLength =
                            (action.relativeSwipe * height * 0.5f).coerceAtLeast(MIN_HEIGHT)

                        if (action.swipeDown) swipeLength *= -1


                        val swipe = Path()
                        // coerceAtLeast to avoid areas like status bar
                        swipe.moveTo(
                            absolutePos.first, absolutePos.second.coerceAtLeast(MIN_HEIGHT)
                        )
                        swipe.lineTo(
                            absolutePos.first,
                            (absolutePos.second + swipeLength).coerceIn(
                                MIN_HEIGHT,
                                height.toFloat()
                            )
                        )

                        val gesture = GestureDescription.Builder()
                            .addStroke(GestureDescription.StrokeDescription(swipe, 0, 100))
                            .build()

                        val status =
                            "${action.relativePos.second};${action.relativePos.first};${action.relativeSwipe};${action.swipeDown}"

                        dispatchGesture(gesture, object : GestureResultCallback() {
                            override fun onCompleted(gestureDescription: GestureDescription) {
                                scope.launch {
                                    client.sendStatus("$status;true")
                                }
                                super.onCompleted(gestureDescription)
                            }

                            override fun onCancelled(gestureDescription: GestureDescription) {
                                scope.launch {
                                    client.sendStatus("$status;false")
                                }
                                super.onCancelled(gestureDescription)
                            }
                        }, null)
                    }
                    .collect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        scope.launch {
            client.close()
        }
        super.onDestroy()
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {}

    companion object {
        const val ACTION_DISABLE_SERVICE = "disable_service"
    }
}