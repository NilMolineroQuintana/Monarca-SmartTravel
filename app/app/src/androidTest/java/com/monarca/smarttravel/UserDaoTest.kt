package com.monarca.smarttravel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.monarca.smarttravel.data.MonarcaDatabase
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
import java.util.Date

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: MonarcaDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MonarcaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertUser_and_getUserById_shouldReturnCorrectUser() = runBlocking {
        val user = User(
            userId = "u1", username = "testuser", birthdate = "01/01/2000",
            email = "test@test.com", phoneNum = "123456789", country = "Espanya",
            address = "Adreça 1", password = "pass", recieveEmails = true
        )
        userDao.insertUser(user)

        val retrievedUser = userDao.getUserById("u1")
        assertNotNull(retrievedUser)
        assertEquals("testuser", retrievedUser?.username)
        assertEquals("Espanya", retrievedUser?.country)
    }

    @Test
    fun getUserByUsername_shouldReturnCorrectUser() = runBlocking {
        val user = User(
            userId = "u2", username = "unique_user", birthdate = "",
            email = "", phoneNum = "", country = "", address = "",
            password = "", recieveEmails = false
        )
        userDao.insertUser(user)

        val retrievedUser = userDao.getUserByUsername("unique_user")
        assertNotNull(retrievedUser)
        assertEquals("u2", retrievedUser?.userId)
    }

    @Test
    fun updateUser_shouldModifyExistingUser() = runBlocking {
        val user = User(
            userId = "u3", username = "old_name", birthdate = "",
            email = "", phoneNum = "", country = "", address = "",
            password = "", recieveEmails = false
        )
        userDao.insertUser(user)

        val updatedUser = user.copy(username = "new_name", country = "França")
        userDao.updateUser(updatedUser)

        val retrievedUser = userDao.getUserById("u3")
        assertEquals("new_name", retrievedUser?.username)
        assertEquals("França", retrievedUser?.country)
    }

    @Test
    fun deleteUser_shouldRemoveUser() = runBlocking {
        val user = User(
            userId = "u4", username = "delete_me", birthdate = "",
            email = "", phoneNum = "", country = "", address = "",
            password = "", recieveEmails = false
        )
        userDao.insertUser(user)
        assertNotNull(userDao.getUserById("u4"))

        userDao.deleteUser(user)
        assertNull(userDao.getUserById("u4"))
    }

    @Test
    fun getAllUsers_shouldReturnAllInsertedUsers() = runBlocking {
        userDao.insertUser(User(userId = "u5", username = "user5", birthdate = "", email = "", phoneNum = "", country = "", address = "", password = "", recieveEmails = false))
        userDao.insertUser(User(userId = "u6", username = "user6", birthdate = "", email = "", phoneNum = "", country = "", address = "", password = "", recieveEmails = false))

        val users = userDao.getAllUsers().first()
        assertEquals(2, users.size)
    }

    @Test
    fun registerAccess_shouldNotThrowException() = runBlocking {
        val user = User(userId = "u7", username = "access_user", birthdate = "", email = "", phoneNum = "", country = "", address = "", password = "", recieveEmails = false)
        userDao.insertUser(user)

        // Verifiquem que la inserció del log no trenca les restriccions de SQL
        userDao.registerAccess("u7", "LOG IN")
        userDao.registerAccess("u7", "LOG OUT")
    }

    // ─── CASCADE ────────────────────────────────────────────────────────────

    @Test
    fun deleteUser_shouldCascadeDeleteTripsAndAccessHistory() = runBlocking {
        val user = User(
            userId = "u_cascade", username = "cascade_user", birthdate = "",
            email = "", phoneNum = "", country = "", address = "",
            password = "", recieveEmails = false
        )
        userDao.insertUser(user)

        userDao.registerAccess(user.userId, "LOG IN")

        val tripDao = db.tripDao()
        val trip = Trip(
            title = "Viatge a esborrar",
            description = "Test cascade",
            dateIn = Date(),
            dateOut = Date(),
            userId = user.userId
        )
        tripDao.addTrip(trip)

        val userTripsBefore = tripDao.getTripsByUser(user.userId).first()
        assertEquals(1, userTripsBefore.size)

        val cursorBefore = db.query("SELECT * FROM acces_history WHERE userId = '${user.userId}'", null)
        assertTrue("Hauria d'existir un registre d'accés", cursorBefore.moveToFirst())
        cursorBefore.close()

        userDao.deleteUser(user)

        assertNull(userDao.getUserById(user.userId))

        val userTripsAfter = tripDao.getTripsByUser(user.userId).first()
        assertTrue("La llista de viatges hauria d'estar buida", userTripsAfter.isEmpty())

        val cursorAfter = db.query("SELECT * FROM acces_history WHERE userId = '${user.userId}'", null)
        assertFalse("No hauria de quedar cap registre d'accés", cursorAfter.moveToFirst())
        cursorAfter.close()
    }
}