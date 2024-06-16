package com.kaajjo.client.presentation.main

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaajjo.client.data.local.datastore.PreferencesDataStore
import com.kaajjo.client.domain.model.SwipeAction
import com.kaajjo.client.domain.service.ClientSocketService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: PreferencesDataStore
) : ViewModel() {
    val serverIp = dataStore.serverIp
    val serverPort = dataStore.serverPort

    fun updateServerIp(ip: String) {
        if (isValidIpAddress(ip)) {
            viewModelScope.launch(Dispatchers.IO) {
                dataStore.updateServerIp(ip)
            }
        }
    }

    fun updateServerPort(port: Int) {
        if (isPortValid(port)) {
            viewModelScope.launch(Dispatchers.IO) {
                dataStore.updateServerPort(port)
            }
        }
    }

    fun isValidIpAddress(ip: String): Boolean {
        val regex = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!\$)|\$)){4}\$".toRegex()
        return regex.matches(ip)
    }

    fun isPortValid(port: Int) = port in (0..65535)
}


