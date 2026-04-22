package com.monarca.smarttravel

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.monarca.smarttravel.data.ItineraryDao
import com.monarca.smarttravel.data.UserDao
import com.monarca.smarttravel.data.repository.AuthRepositoryImpl
import com.monarca.smarttravel.data.repository.ItineraryRepositoryImpl
import com.monarca.smarttravel.data.repository.PreferencesManager
import com.monarca.smarttravel.data.repository.TripRepositoryImpl
import com.monarca.smarttravel.domain.interfaces.AuthRepository
import com.monarca.smarttravel.domain.interfaces.ItineraryRepository
import com.monarca.smarttravel.domain.interfaces.TripRepository
import com.google.firebase.auth.FirebaseAuth
import com.monarca.smarttravel.data.MonarcaDatabase
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