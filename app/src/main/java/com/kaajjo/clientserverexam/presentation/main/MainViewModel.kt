package com.kaajjo.clientserverexam.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaajjo.clientserverexam.data.local.datastore.PreferencesDataStore
import com.kaajjo.clientserverexam.di.component.Component
import com.kaajjo.clientserverexam.di.component.ServerComponent
import com.kaajjo.clientserverexam.domain.repository.ActionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: PreferencesDataStore,
    private val actionRepository: ActionRepository
) : ViewModel() {
    private var serverComponent: ServerComponent? = null

    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    val serverPort = dataStore.serverPort
    val logs = actionRepository.get()

    fun startServer(port: Int, context: Context) {
        if (port !in (0..65535)) return

        viewModelScope.launch(Dispatchers.IO) {
            serverComponent = Component.createServerComponent(
                port = port,
                context = context
            )
            serverComponent?.server?.start()
            _isRunning.value = serverComponent != null
        }
    }

    fun stopServer() {
        serverComponent?.server?.stop()
        _isRunning.value = false
    }

    fun updatePort(port: Int) {
        if (port in (0..65535)) {
            viewModelScope.launch(Dispatchers.IO) {
                dataStore.updateServerPort(port)
            }
        }
    }
}