# Sprint 03 – Execution & Review

## 1. Obtained results

### Comparison with Sprint Goal:
The main goal of Sprint 03 was successfully achieved. The in-memory fake data sources from Sprint 02 have been completely replaced with a persistent SQLite database using **Room**. Additionally, **Firebase Authentication** has been integrated to handle user registration (with email verification), login, and password recovery. Trips and itinerary items are now correctly scoped to the authenticated user's unique ID.

---

## 2. Tasks completed

|    ID    | Completed | Comments                                                                                            |
|:--------:|:---------:|-----------------------------------------------------------------------------------------------------|
|  **T1**  |           | **SQLite Persistence (Room)**                                                                       |
|   T1.1   |    Yes    | `MonarcaDatabase` implemented with support for `User`, `Trip`, `ItineraryItem`, and `AccesHistory`. |
|   T1.2   |    Yes    | Room entities defined with `TypeConverters` for `Date` to `Long` mapping.                           |
|   T1.3   |    Yes    | DAOs created (`TripDao`, `ItineraryDao`, `UserDao`) with reactive `Flow` queries.                   |
|   T1.4   |    Yes    | Full CRUD operations implemented in the repository layer using DAOs.                                |
|   T1.5   |    Yes    | `TripViewModel` and `ItineraryViewModel` migrated to use Room-backed repositories.                  |
|   T1.6   |    Yes    | UI recomposes automatically using `collectAsState()` on repository flows.                           |
|  **T2**  |           | **Login and Logout**                                                                                |
|   T2.1   |    Yes    | Firebase project connected and `google-services.json` configured.                                   |
|   T2.2   |    Yes    | Login screen designed with email/password validation.                                               |
|   T2.3   |    Yes    | Firebase Authentication flow implemented; users are redirected to Home on success.                  |
|   T2.4   |    Yes    | Logout action implemented in Preferences, clearing session and navigating to Login.                 |
|   T2.5   |    Yes    | Comprehensive logging added to `AuthRepositoryImpl` for all auth events.                            |
| ****T3** |           | **Register and Recover password**                                                                   |
|   T3.1   |    Yes    | Registration screen implemented with all required user profile fields.                              |
|   T3.2   |    Yes    | Registration flow includes Firebase `sendEmailVerification` requirement.                            |
|   T3.3   |    Yes    | "Forgot Password" functionality implemented via Firebase.                                           |
|  **T4**  |           | **Persist user information and trip**                                                               |
|   T4.1   |    Yes    | Local `User` table in Room synced with Firebase UID; unique username check implemented.             |
|   T4.2   |    Yes    | Trips are filtered by `userId` at the DAO and ViewModel levels.                                     |
|   T4.3   |    Yes    |                                                                                                     |
|   T4.4   |    Yes    | `acces_history` table records login and logout events with timestamps.                              |
|  **T5**  |           | **Testing and Debugging**                                                                           |
|   T5.1   |    Yes    |                                                                                                     |
|   T5.2   |    Yes    |                                                                                                     |
|   T5.3   |    Yes    |                                                                                                     |
|   T5.4   |    Yes    |                                                                                                     |

---

## 3. Deviations
No deviations have been made in the development and all is going acording to the planning.


---

## 4. Retrospective

### What worked well
- **Hilt Integration:** Dependency injection for Room DAOs and Firebase instances made the transition from fake data sources very smooth.
- **Reactive UI:** Using `Flow` in Room DAOs significantly simplified the UI logic, as screens update automatically when data changes.
- **Firebase Auth:** Using a managed service for authentication saved significant development time regarding security and password recovery.

### What didn't work
- **Date Precision:** SQLite does not store `Date` objects directly; managing precision during `Long` conversion required extra care to avoid off-by-one errors in trip ranges.
- **Initial Sync:** Ensuring the local Room user exists before attempting to load trips after a fresh Firebase login required adding additional state checks in `AuthViewModel`.

### What we will improve in the next sprint


---

## 5. Team Self-Assessment (0-10)

**Score:** 10

**Justification:** All planned features for the persistence and authentication layers were implemented. The app is now much more robust and ready for real-world usage.

---

## 6. Test Results

### Room DAO Tests

#### TripDaoTest

| Test | Result |
|------|--------|
| `addTrip should add trip to list` | ✅ Pass |
| `getTripById with valid id should return correct trip` | ✅ Pass |
| `getTripById with invalid id should return null` | ✅ Pass |
| `getAllTrips should return all inserted trips` | ✅ Pass |
| `updateTrip should modify existing trip` | ✅ Pass |
| `updateTrip with non existing id should return zero rows affected` | ✅ Pass |
| `deleteTrip should remove only that trip` | ✅ Pass |
| `deleteTrip with non existing id should return zero rows affected` | ✅ Pass |
| `getTripsByUser should return only user trips` | ✅ Pass |
| `updateImage should update trip image` | ✅ Pass |
| `getNextUpcomingTrip should return closest future trip` | ✅ Pass |

#### ItineraryDaoTest

| Test | Result |
|------|--------|
| `addItem should add item to trip` | ✅ Pass |
| `getItemsByTrip should return only that trips items` | ✅ Pass |
| `getItemsByTrip with invalid tripId should return empty list` | ✅ Pass |
| `getItemById with valid id should return correct item` | ✅ Pass |
| `getItemById with invalid id should return null` | ✅ Pass |
| `updateItem should modify existing item` | ✅ Pass |
| `updateItem with non existing id should return zero rows affected` | ✅ Pass |
| `deleteItem should remove only that item` | ✅ Pass |
| `deleteItem with non existing id should return zero rows affected` | ✅ Pass |
| `deleteTrip should cascade delete its items` | ✅ Pass |

#### UserDaoTest

| Test | Result |
|------|--------|
| `insertUser and getUserById should return correct user` | ✅ Pass |
| `getUserByUsername should return correct user` | ✅ Pass |
| `updateUser should modify existing user` | ✅ Pass |
| `deleteUser should remove user` | ✅ Pass |
| `getAllUsers should return all inserted users` | ✅ Pass |
| `registerAccess should not throw exception` | ✅ Pass |
| `deleteUser should cascade delete trips and access history` | ✅ Pass |

### Fixes applied during testing

- Registration of access logs does not break SQL constraints for valid users.
- Implemented cascade deletion carefully to ensure that deleting a user correctly removes all associated trips and access history records from the local Room database to prevent orphaned data.
