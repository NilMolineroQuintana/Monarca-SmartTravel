package com.example.monarcasmarttravel

import com.example.monarcasmarttravel.data.repository.ItineraryItemRepositoryImpl
import com.example.monarcasmarttravel.domain.interfaces.ItineraryItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ItineraryItemModule {

    @Binds
    @Singleton
    abstract fun bindItineraryItemRepository(
        impl: ItineraryItemRepositoryImpl
    ): ItineraryItemRepository
}