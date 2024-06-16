package com.kaajjo.client.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class PreferencesDataStore(context: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.createDataStore

    private val serverIpKey = stringPreferencesKey("server_ip")
    val serverIp = dataStore.data.map { prefs -> prefs[serverIpKey] ?: "" }
    suspend fun updateServerIp(ip: String) {
        dataStore.edit { prefs -> prefs[serverIpKey] = ip }
    }

    private val serverPortKey = intPreferencesKey("server_port")
    val serverPort = dataStore.data.map { prefs -> prefs[serverPortKey] ?: 8000 }
    suspend fun updateServerPort(port: Int) {
        dataStore.edit { prefs -> prefs[serverPortKey] = port }
    }
}