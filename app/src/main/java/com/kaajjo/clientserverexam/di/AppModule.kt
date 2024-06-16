package com.kaajjo.clientserverexam.di

import android.app.Application
import android.content.Context
import com.kaajjo.clientserverexam.data.local.database.AppDatabase
import com.kaajjo.clientserverexam.data.local.database.dao.ActionDao
import com.kaajjo.clientserverexam.data.local.database.repository.ActionRepositoryImpl
import com.kaajjo.clientserverexam.data.local.datastore.PreferencesDataStore
import com.kaajjo.clientserverexam.domain.repository.ActionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        PreferencesDataStore(context)

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application) = AppDatabase.getInstance(context = app)

    @Provides
    @Singleton
    fun provideActionDao(appDatabase: AppDatabase) = appDatabase.actionDao()

    @Singleton
    @Provides
    fun provideActionRepository(actionDao: ActionDao): ActionRepository =
        ActionRepositoryImpl(actionDao)
}