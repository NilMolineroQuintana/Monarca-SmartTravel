package com.example.monarcasmarttravel

import android.content.Context
import android.content.SharedPreferences
import com.example.monarcasmarttravel.data.repository.ItineraryItemRepositoryImpl
import com.example.monarcasmarttravel.data.repository.PreferencesManager
import com.example.monarcasmarttravel.data.repository.TripRepositoryImpl
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
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
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("monarca_preferences", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferencesManager(
        sharedPreferences: SharedPreferences,
        @ApplicationContext context: Context
    ): PreferencesManager =
        PreferencesManager(sharedPreferences, context)

    @Provides
    @Singleton
    fun provideItineraryItemRepository(): ItineraryItemRepository =
        ItineraryItemRepositoryImpl()

    @Provides
    @Singleton
    fun provideTripRepository(): TripRepository =
        TripRepositoryImpl()
}
