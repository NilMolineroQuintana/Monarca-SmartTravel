package com.monarca.smarttravel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monarca.smarttravel.data.ImageDao
import com.monarca.smarttravel.data.MonarcaDatabase
import com.monarca.smarttravel.data.TripDao
import com.monarca.smarttravel.data.UserDao
import com.monarca.smarttravel.domain.model.Image
import com.monarca.smarttravel.domain.model.Trip
import com.monarca.smarttravel.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ImageDaoTest {

    private lateinit var db: MonarcaDatabase
    private lateinit var imageDao: ImageDao
    private lateinit var tripDao: TripDao
    private lateinit var userDao: UserDao

    private var testTripId: Int = 0
    private val testUserId = "user_images"

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MonarcaDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        imageDao = db.imageDao()
        tripDao = db.tripDao()
        userDao = db.userDao()

        userDao.insertUser(
            User(userId = testUserId, username = "nil", birthdate = "", email = "", phoneNum = "", country = "", address = "", password = "", recieveEmails = false)
        )
        testTripId = tripDao.addTrip(
            Trip(title = "Roma, Italia", description = "Test Imatges", dateIn = Date(), dateOut = Date(), userId = testUserId)
        ).toInt()
    }

    @After
    fun tearDown() {
        db.close()
    }

    // ─── ADD ────────────────────────────────────────────────────────────────

    @Test
    fun insertImage_shouldAddImageToTrip() = runBlocking {
        val image = Image(tripId = testTripId, imagePath = "/path/to/image1.jpg", dateUploaded = Date())
        val id = imageDao.insertImage(image)

        assertTrue(id > 0)

        val images = imageDao.getImagesByTrip(testTripId).first()
        assertEquals(1, images.size)
        assertEquals("/path/to/image1.jpg", images.first().imagePath)
    }

    // ─── GET ────────────────────────────────────────────────────────────────

    @Test
    fun getImagesByTrip_shouldReturnOnlyThatTripsImages() = runBlocking {
        val otherTripId = tripDao.addTrip(
            Trip(title = "París", description = "", dateIn = Date(), dateOut = Date(), userId = testUserId)
        ).toInt()

        imageDao.insertImage(Image(tripId = testTripId, imagePath = "/path/img1.jpg", dateUploaded = Date()))
        imageDao.insertImage(Image(tripId = otherTripId, imagePath = "/path/img2.jpg", dateUploaded = Date()))

        val images = imageDao.getImagesByTrip(testTripId).first()

        assertEquals(1, images.size)
        assertTrue(images.all { it.tripId == testTripId })
        assertEquals("/path/img1.jpg", images.first().imagePath)
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────

    @Test
    fun deleteImage_shouldRemoveSpecificImage() = runBlocking {
        val image = Image(tripId = testTripId, imagePath = "/path/to/delete.jpg", dateUploaded = Date())
        val imageId = imageDao.insertImage(image).toInt()
        val imageToDelete = image.copy(id = imageId) // Instancia con el ID generado

        val deletedRows = imageDao.deleteImage(imageToDelete)
        assertEquals(1, deletedRows)

        val images = imageDao.getImagesByTrip(testTripId).first()
        assertTrue(images.isEmpty())
    }

    // ─── CASCADE ────────────────────────────────────────────────────────────

    @Test
    fun deleteTrip_shouldCascadeDeleteItsImages() = runBlocking {
        imageDao.insertImage(Image(tripId = testTripId, imagePath = "/path/img_cascade.jpg", dateUploaded = Date()))

        // Al eliminar el viaje, se deberían borrar sus imágenes por el CASCADE
        tripDao.deleteTripById(testTripId)

        val images = imageDao.getImagesByTrip(testTripId).first()
        assertTrue("La lista de imágenes debería estar vacía tras borrar el viaje", images.isEmpty())
    }
}