package com.kaajjo.client.di

import android.content.Context
import androidx.datastore.preferences.PreferencesMapCompat
import com.kaajjo.client.data.local.datastore.PreferencesDataStore
import com.kaajjo.client.data.remote.service.ClientSocketServiceImpl
import com.kaajjo.client.domain.service.ClientSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
        }
    }

    @Provides
    @Singleton
    fun provideClientSocketService(client: HttpClient): ClientSocketService = ClientSocketServiceImpl(client)

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        PreferencesDataStore(context)
}