package com.monarca.smarttravel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monarca.smarttravel.data.ItineraryDao
import com.monarca.smarttravel.data.MonarcaDatabase
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.data.UserDao
import com.monarca.smarttravel.domain.model.ItineraryItem
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.domain.model.User
import com.monarca.smarttravel.ui.screens.trip.PlanType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class ItineraryDaoTest {

    private lateinit var db: MonarcaDatabase
    private lateinit var itineraryDao: ItineraryDao
    private lateinit var tripDao: TripDao
    private lateinit var userDao: UserDao

    private var testTripId: Int = 0

    private val tripDateIn  = Calendar.getInstance().apply { set(2026, Calendar.MARCH, 23, 0, 0) }.time
    private val tripDateOut = Calendar.getInstance().apply { set(2026, Calendar.MARCH, 30, 23, 59) }.time
    private val itemDate    = Calendar.getInstance().apply { set(2026, Calendar.MARCH, 25, 10, 0) }.time

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MonarcaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        itineraryDao = db.itineraryDao()
        tripDao      = db.tripDao()
        userDao      = db.userDao()

        userDao.insertUser(
            User(userId = "u1", username = "testuser", birthdate = "", email = "", phoneNum = "", country = "", address = "", password = "", recieveEmails = false)
        )
        testTripId = tripDao.addTrip(
            Trip(title = "Kyoto, Japó", description = "Test", dateIn = tripDateIn, dateOut = tripDateOut, userId = "u1")
        ).toInt()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun locationItem(name: String = "Sagrada Família", tripId: Int = testTripId) = ItineraryItem(
        tripId       = tripId,
        type         = PlanType.LOCATION,
        locationName = name,
        address      = "Carrer de Mallorca, 401, Barcelona",
        checkInDate  = itemDate,
        price        = 26.0
    )

    // ─── ADD ────────────────────────────────────────────────────────────────

    @Test
    fun addItem_shouldAddItemToTrip() = runBlocking {
        val id = itineraryDao.addItem(locationItem())
        assertTrue(id > 0)

        val items = itineraryDao.getItemsByTrip(testTripId).first()
        assertEquals(1, items.size)
        assertEquals("Sagrada Família", items.first().locationName)
    }

    // ─── GET ────────────────────────────────────────────────────────────────

    @Test
    fun getItemsByTrip_shouldReturnOnlyThatTripsItems() = runBlocking {
        // Segon viatge per verificar l'aïllament
        val otherId = tripDao.addTrip(
            Trip(title = "Altre viatge", description = "Test", dateIn = tripDateIn, dateOut = tripDateOut, userId = "u1")
        ).toInt()

        itineraryDao.addItem(locationItem(tripId = testTripId))
        itineraryDao.addItem(locationItem(name = "Autre lloc", tripId = otherId))

        val items = itineraryDao.getItemsByTrip(testTripId).first()
        assertEquals(1, items.size)
        assertTrue(items.all { it.tripId == testTripId })
    }

    @Test
    fun getItemsByTrip_withInvalidTripId_shouldReturnEmptyList() = runBlocking {
        val items = itineraryDao.getItemsByTrip(9999).first()
        assertTrue(items.isEmpty())
    }

    @Test
    fun getItemById_withValidId_shouldReturnCorrectItem() = runBlocking {
        val id = itineraryDao.addItem(locationItem()).toInt()

        val item = itineraryDao.getItemById(id)
        assertNotNull(item)
        assertEquals("Sagrada Família", item!!.locationName)
    }

    @Test
    fun getItemById_withInvalidId_shouldReturnNull() = runBlocking {
        val item = itineraryDao.getItemById(9999)
        assertNull(item)
    }

    // ─── UPDATE ─────────────────────────────────────────────────────────────

    @Test
    fun updateItem_shouldModifyExistingItem() = runBlocking {
        val id = itineraryDao.addItem(locationItem()).toInt()
        val existing = itineraryDao.getItemById(id)!!

        itineraryDao.updateItem(existing.copy(locationName = "Aeroport El Prat", price = 999.0))

        val updated = itineraryDao.getItemById(id)
        assertEquals("Aeroport El Prat", updated?.locationName)
        assertEquals(999.0, updated?.price)
    }

    @Test
    fun updateItem_withNonExistingId_shouldReturnZeroRowsAffected() = runBlocking {
        val fakeItem = locationItem().copy(id = 9999)
        val rows = itineraryDao.updateItem(fakeItem)
        assertEquals(0, rows)
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────

    @Test
    fun deleteItem_shouldRemoveOnlyThatItem() = runBlocking {
        val id1 = itineraryDao.addItem(locationItem("Lloc 1")).toInt()
        itineraryDao.addItem(locationItem("Lloc 2"))

        itineraryDao.deleteItem(id1)

        val items = itineraryDao.getItemsByTrip(testTripId).first()
        assertEquals(1, items.size)
        assertEquals("Lloc 2", items.first().locationName)
        assertNull(itineraryDao.getItemById(id1))
    }

    @Test
    fun deleteItem_withNonExistingId_shouldReturnZeroRowsAffected() = runBlocking {
        val rows = itineraryDao.deleteItem(9999)
        assertEquals(0, rows)
    }

    // ─── CASCADE ────────────────────────────────────────────────────────────

    @Test
    fun deleteTrip_shouldCascadeDeleteItsItems() = runBlocking {
        itineraryDao.addItem(locationItem())
        itineraryDao.addItem(locationItem("Segon lloc"))

        tripDao.deleteTripById(testTripId)

        val items = itineraryDao.getItemsByTrip(testTripId).first()
        assertTrue(items.isEmpty())
    }
}