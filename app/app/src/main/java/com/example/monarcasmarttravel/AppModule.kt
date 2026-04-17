package com.example.monarcasmarttravel

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.monarcasmarttravel.data.ItineraryDao
import com.example.monarcasmarttravel.data.MonarcaDatabase
import com.example.monarcasmarttravel.data.UserDao
import com.example.monarcasmarttravel.data.repository.AuthRepositoryImpl
import com.example.monarcasmarttravel.data.repository.ItineraryRepositoryImpl
import com.example.monarcasmarttravel.data.repository.PreferencesManager
import com.example.monarcasmarttravel.data.repository.TripRepositoryImpl
import com.example.monarcasmarttravel.domain.interfaces.AuthRepository
import com.example.monarcasmarttravel.domain.interfaces.ItineraryRepository
import com.example.monarcasmarttravel.domain.interfaces.TripRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideItineraryRepository(
        itineraryDao: ItineraryDao
    ): ItineraryRepository =
        ItineraryRepositoryImpl(itineraryDao)

    @Provides
    @Singleton
    fun provideTripRepository(): TripRepository =
        TripRepositoryImpl()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, userDao: UserDao): AuthRepository =
        AuthRepositoryImpl(auth, userDao)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MonarcaDatabase {
        return Room.databaseBuilder(
            context,
            MonarcaDatabase::class.java,
            "monarca_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: MonarcaDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideItineraryDao(db: MonarcaDatabase): ItineraryDao {
        return db.itineraryDao()
    }
}
