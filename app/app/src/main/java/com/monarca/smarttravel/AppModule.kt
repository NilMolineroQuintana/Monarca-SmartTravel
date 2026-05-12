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
import com.monarca.smarttravel.data.ImageDao
import com.monarca.smarttravel.data.MonarcaDatabase
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.data.repository.ImageRepositoryImpl
import com.monarca.smarttravel.data.remote.HotelAPIService
import com.monarca.smarttravel.data.repository.BookingRepositoryImpl
import com.monarca.smarttravel.domain.interfaces.BookingRepository
import com.monarca.smarttravel.domain.interfaces.ImageRepository
import com.monarca.smarttravel.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

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
        intineraryDao: ItineraryDao
    ): ItineraryRepository =
        ItineraryRepositoryImpl(intineraryDao)

    @Provides
    @Singleton
    fun provideTripRepository(
        tripDao: TripDao,
        intineraryDao: ItineraryDao
    ): TripRepository =
        TripRepositoryImpl(
            tripDao,
            intineraryDao
        )

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, userDao: UserDao): AuthRepository =
        AuthRepositoryImpl(auth, userDao)

    @Provides
    @Singleton
    fun provideImageRepository(imageDao: ImageDao): ImageRepository =
        ImageRepositoryImpl(imageDao)

    @Provides
    @Singleton
    fun provideBookingRepository(hotelAPIService: HotelAPIService): BookingRepository =
        BookingRepositoryImpl(hotelAPIService)

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

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): HotelAPIService =
        retrofit.create(HotelAPIService::class.java)

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

    @Singleton
    @Provides
    fun provideTripDao(db: MonarcaDatabase): TripDao {
        return db.tripDao()
    }

    @Singleton
    @Provides
    fun provideImageDao(db: MonarcaDatabase): ImageDao {
        return db.imageDao()
    }
}