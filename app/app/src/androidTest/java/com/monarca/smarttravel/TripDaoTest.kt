package com.monarca.smarttravel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monarca.smarttravel.data.MonarcaDatabase
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.data.UserDao
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var db: MonarcaDatabase
    private lateinit var tripDao: TripDao
    private lateinit var userDao: UserDao

    private val testUser = User(
        userId = "test-user-id",
        username = "testuser",
        birthdate = "01/01/1990",
        email = "",
        phoneNum = "123456789",
        country = "Espanya",
        address = "Carrer Test 1",
        password = "",
        recieveEmails = false
    )

    private val validDateIn  = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 1,  9,  0) }.time
    private val validDateOut = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 10, 18, 0) }.time

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MonarcaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tripDao  = db.tripDao()
        userDao  = db.userDao()
        userDao.insertUser(testUser)
    }

    @After
    fun tearDown() {
        db.close()
    }

    // ─── ADD ────────────────────────────────────────────────────────────────

    @Test
    fun addTrip_shouldAddTripToList() = runBlocking {
        val trip = Trip(
            title       = "Lisboa, Portugal",
            description = "Viatge a Lisboa",
            dateIn      = validDateIn,
            dateOut     = validDateOut,
            userId      = testUser.userId
        )
        val id = tripDao.addTrip(trip)
        assertTrue(id > 0)

        val trips = tripDao.getAllTrips().first()
        assertEquals(1, trips.size)
        assertEquals("Lisboa, Portugal", trips.first().title)
    }

    // ─── GET ────────────────────────────────────────────────────────────────

    @Test
    fun getTripById_withValidId_shouldReturnCorrectTrip() = runBlocking {
        val id = tripDao.addTrip(
            Trip(title = "Kyoto, Japó", description = "Test", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId)
        ).toInt()

        val trip = tripDao.getTripById(id)
        assertNotNull(trip)
        assertEquals("Kyoto, Japó", trip!!.title)
    }

    @Test
    fun getTripById_withInvalidId_shouldReturnNull() = runBlocking {
        val trip = tripDao.getTripById(9999)
        assertNull(trip)
    }

    @Test
    fun getAllTrips_shouldReturnAllInsertedTrips() = runBlocking {
        tripDao.addTrip(Trip(title = "A", description = "desc", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId))
        tripDao.addTrip(Trip(title = "B", description = "desc", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId))

        val trips = tripDao.getAllTrips().first()
        assertEquals(2, trips.size)
        assertTrue(trips.all { it.id > 0 })
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────

    @Test
    fun updateTrip_shouldModifyExistingTrip() = runBlocking {
        val id = tripDao.addTrip(
            Trip(title = "Viatge original", description = "Desc", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId)
        ).toInt()

        val existing = tripDao.getTripById(id)!!
        val newDateOut = Calendar.getInstance().apply { set(2026, Calendar.SEPTEMBER, 15, 18, 0) }.time
        tripDao.updateTrip(existing.copy(title = "Viatge actualitzat", description = "Nova descripció", dateOut = newDateOut))

        val updated = tripDao.getTripById(id)
        assertEquals("Viatge actualitzat", updated?.title)
        assertEquals("Nova descripció", updated?.description)
    }

    @Test
    fun updateTrip_withNonExistingId_shouldReturnZeroRowsAffected() = runBlocking {
        val fake = Trip(id = 9999, title = "Inexistent", description = "X", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId)
        val rows = tripDao.updateTrip(fake)
        assertEquals(0, rows)
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────

    @Test
    fun deleteTrip_shouldRemoveOnlyThatTrip() = runBlocking {
        tripDao.addTrip(Trip(title = "Quedarà", description = "desc", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId))
        val idToDelete = tripDao.addTrip(
            Trip(title = "S'eliminarà", description = "desc", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId)
        ).toInt()

        tripDao.deleteTripById(idToDelete)

        val trips = tripDao.getAllTrips().first()
        assertEquals(1, trips.size)
        assertEquals("Quedarà", trips.first().title)
        assertNull(tripDao.getTripById(idToDelete))
    }

    @Test
    fun deleteTrip_withNonExistingId_shouldReturnZeroRowsAffected() = runBlocking {
        val rows = tripDao.deleteTripById(9999)
        assertEquals(0, rows)
    }

    // ─── CUSTOM QUERIES ─────────────────────────────────────────────────────

    @Test
    fun getTripsByUser_shouldReturnOnlyUserTrips() = runBlocking {
        // Creem un segon usuari per comprovar el filtre
        val user2 = User(
            userId = "user2-id", username = "usuari2", birthdate = "",
            email = "", phoneNum = "", country = "", address = "",
            password = "", recieveEmails = false
        )
        userDao.insertUser(user2)

        // Afegim viatges per a diferents usuaris
        tripDao.addTrip(Trip(title = "Viatge Usuari 1", description = "", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId))
        tripDao.addTrip(Trip(title = "Viatge Usuari 2", description = "", dateIn = validDateIn, dateOut = validDateOut, userId = user2.userId))

        val userTrips = tripDao.getTripsByUser(testUser.userId).first()
        assertEquals(1, userTrips.size)
        assertEquals("Viatge Usuari 1", userTrips.first().title)
    }

    @Test
    fun updateImage_shouldUpdateTripImage() = runBlocking {
        val tripId = tripDao.addTrip(
            Trip(title = "Test Imatge", description = "", dateIn = validDateIn, dateOut = validDateOut, userId = testUser.userId)
        ).toInt()

        val rowsAffected = tripDao.updateImage(tripId, 12345)
        assertEquals(1, rowsAffected)

        val updatedTrip = tripDao.getTripById(tripId)
        assertEquals(12345, updatedTrip?.imageResId)
    }

    @Test
    fun getNextUpcomingTrip_shouldReturnClosestFutureTrip() = runBlocking {
        val now = System.currentTimeMillis()

        // Dates
        val pastDateIn = java.util.Date(now - 100000)
        val pastDateOut = java.util.Date(now - 50000)

        val futureDateIn1 = java.util.Date(now + 50000)
        val futureDateOut1 = java.util.Date(now + 100000)

        val futureDateIn2 = java.util.Date(now + 200000)
        val futureDateOut2 = java.util.Date(now + 300000)

        // Inserim viatge passat i dos futurs
        tripDao.addTrip(Trip(title = "Passat", description = "", dateIn = pastDateIn, dateOut = pastDateOut, userId = testUser.userId))
        tripDao.addTrip(Trip(title = "Futur Llunyà", description = "", dateIn = futureDateIn2, dateOut = futureDateOut2, userId = testUser.userId))
        tripDao.addTrip(Trip(title = "Futur Proper", description = "", dateIn = futureDateIn1, dateOut = futureDateOut1, userId = testUser.userId))

        // Ha de retornar el futur més proper
        val upcoming = tripDao.getNextUpcomingTrip(now)
        assertNotNull(upcoming)
        assertEquals("Futur Proper", upcoming?.title)
    }
}