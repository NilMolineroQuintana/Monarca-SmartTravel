# Sprint 04 – Execution & Review

## 1. Obtained results

### Comparison with Sprint Goal:

The sprint goal was fully achieved. Retrofit has been integrated and configured to connect the app with the external hotel REST API. Hotel search, room listing, booking confirmation, reservation management and cancellation are all implemented end-to-end. Reservation data is persisted locally in Room as a new trip, enriched with all hotel and room metadata. The image gallery feature per trip is fully functional, allowing users to attach photos from the camera or the device gallery, store them locally, and browse them in the album screen. Trip cards visually distinguish trips that originate from a hotel reservation. Unit tests cover the repository layer using Retrofit mocks. Additional improvements include trip sorting by date proximity, city drawables as trip covers, string externalization to Spanish and English, removal of unused domain models and locked portrait orientation.

---

## 2. Tasks completed

|   ID   | Completed | Comments                                                                                                                                                                                                                                                                                                                                                                                                       |
|:------:|:---------:|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **T1** |           | **Retrofit Configuration**                                                                                                                                                                                                                                                                                                                                                                                     |
|  T1.1  |    Yes    | Retrofit added as a dependency and configured in `AppModule` with `GsonConverterFactory`, `OkHttpClient` with a logging interceptor, and the hotel API base URL from `Constants`.                                                                                                                                                                                                                              |
|  T1.2  |    Yes    | Remote data models (`Hotel`, `Room`, `HotelResponse`, `BookingData`, `BookingResponse`) created as plain data classes. `HotelAPIService` interface defines all required endpoints: `checkAvailability`, `bookRoom` and `cancelReservation`, following the MVVM structure.                                                                                                                                      |
|  T1.3  |    Yes    | `BookingRepository` interface and `BookingRepositoryImpl` created, abstracting all API calls behind the repository pattern and wired through Hilt in `AppModule`.                                                                                                                                                                                                                                              |
|  T1.4  |    Yes    | `BookingTest` written using Mockito to mock `HotelAPIService`. Covers 7 scenarios: success and failure for `getAvailable`, `bookRoom`, and `cancelReservation`, plus exception handling for cancellation.                                                                                                                                                                                                      |
| **T2** |           | **Search and Booking Screens**                                                                                                                                                                                                                                                                                                                                                                                 |
|  T2.1  |    Yes    | `BookingScreen` implemented with a city selector (Paris, Barcelona, London) using visual `CityCard` components and two `DateField` date pickers with past-date blocking and range validation. The search triggers the `getAvailable` API call via `BookingViewModel`.                                                                                                                                          |
|  T2.2  |    Yes    | `HotelListScreen` displays the list of hotels returned by the API using animated `HotelComponent` cards with image, name, address and star rating. `HotelRoomScreen` shows the hotel cover image and the list of available rooms; selecting a room enters detail mode with a `HorizontalPager` image gallery and a book button. A "How to get there" button opens Google Maps with the hotel address.          |
|  T2.3  |    Yes    | `BookingConfirmationScreen` shows a summary of the selected hotel, room, dates and total price. On confirmation, `TripViewModel.addTripFromBooking` creates a new `Trip` row in Room with all reservation metadata (`reservationId`, `hotelName`, `hotelImageUrl`, `roomType`, `totalPrice`) and an associated `ItineraryItem` of type `HOTEL` marked as `isFromReservation = true` to prevent manual editing. |
|  T2.4  |    Yes    | Hotel cover images are loaded with Coil (`AsyncImage`) in `HotelListScreen` and `HotelRoomScreen`. Room images are displayed in a `HorizontalPager` both in the room list card and in the full detail view.                                                                                                                                                                                                    |
| **T3** |           | **Add Images / Gallery to Trip**                                                                                                                                                                                                                                                                                                                                                                               |
|  T3.1  |    Yes    | `AlbumScreen` integrates `ActivityResultContracts.PickMultipleVisualMedia` for gallery access and `ActivityResultContracts.TakePicture` for the camera, with runtime permission handling via `ActivityResultContracts.RequestPermission`. Both capture buttons are exposed as FABs. Multi-selection mode activated on long-press allows batch deletion.                                                        |
|  T3.2  |    Yes    | Selected images are copied to the app's external files directory (`Environment.DIRECTORY_PICTURES`) via `saveImageToInternalStorage`. File paths are persisted in the `images` Room table through `ImageViewModel` and `ImageRepositoryImpl`.                                                                                                                                                                  |
|  T3.3  |    Yes    | The album screen displays the trip-specific gallery in a 3-column `LazyVerticalGrid`. Tapping an image opens a full-screen zoomable viewer (`Dialog` + `transformable` modifier with pinch-to-zoom and pan).                                                                                                                                                                                                   |
| **T4** |           | **List and Cancel Reservations**                                                                                                                                                                                                                                                                                                                                                                               |
|  T4.1  |    Yes    | `ReservationsScreen` lists all trips with a non-null `reservationId`, filtered reactively from the `reservations` `StateFlow` in `TripViewModel`. Each entry shows hotel image, name, room type, check-in date and reservation ID, linking back to the associated trip.                                                                                                                                        |
|  T4.2  |    Yes    | Deleting a reservation triggers `TripViewModel.deleteTrip`, which first calls `BookingRepository.cancelReservation` against the API endpoint and then removes the trip and all associated data (itinerary items, images) from Room via cascade delete. A confirmation `PopUp` is shown before the action is executed.                                                                                          |
|  T4.3  |    Yes    | `ReservationCard` loads the hotel image with Coil using the remote URL stored in `Trip.hotelImageUrl`. The room type is displayed with title-case formatting below the hotel name, following the reference style.                                                                                                                                                                                              |
|  T4.4  |    Yes    | `TripCard` detects trips with a non-null `reservationId` and displays a pill badge with the hotel icon and room type below the trip title. The card background and gradient adapt to the hotel image when available, clearly indicating which trips include a hotel reservation.                                                                                                                               |

---

## 3. Deviations

The hotel API only exposes three cities (Paris, Barcelona and London), so the city selector was intentionally limited to those three options. City codes are resolved internally via a `cities` map (`"Paris" → "PAR"`, etc.) before triggering the API call.

The `checkOutDate` field for itinerary items was kept commented out (as in Sprint 02) since the booking flow derives the end date from the reservation dates, which are already stored in `Trip.dateOut`. This avoids duplicating state and keeps the `ItineraryItem` model consistent with the rest of the app.

City-specific cover images for trips created from reservations (Paris, Barcelona, London) are resolved from local drawable resources to ensure visual consistency even when the remote hotel image is unavailable.

---

## 4. Retrospective

### What worked well
- The repository pattern established in previous sprints made it trivial to add the `BookingRepository` alongside the existing Room-backed repositories, with Hilt wiring everything together without any changes to the ViewModel layer outside of `BookingViewModel`.
- Using `StateFlow` in `BookingViewModel` to share hotel, room and date state across the booking flow (search → list → rooms → confirmation) via `getBackStackEntry("book")` kept the navigation graph clean and avoided prop-drilling between composables.
- The `isFromReservation` flag on `ItineraryItem` proved effective for distinguishing API-generated items from user-created ones, preventing the edit/delete menu from appearing on hotel reservation items without any additional state management.
- Coil handled remote image loading with placeholder and error fallback correctly in all screens, including the `HorizontalPager` room gallery.
- The existing `saveImageToInternalStorage` utility and `ImageViewModel` / `ImageRepositoryImpl` pair from the gallery tasks were straightforward to reuse and required no structural changes to the Room schema.
- Unit testing with Mockito allowed comprehensive coverage of the repository, ViewModel, and DAO layers (31 tests total across 4 test classes).
- The image picker integration (`PickMultipleVisualMedia` + `TakePicture`) with proper permission handling worked reliably across different Android versions.

### What didn't work
- The hotel API occasionally returns an empty room list for certain city/date combinations, which required adding an explicit empty-state handling in `HotelRoomScreen` to avoid showing a blank screen without feedback.
- `HorizontalPager` inside a `LazyColumn` item requires careful height constraints to avoid infinite measurement issues; the room cards needed an explicit `.height(100.dp)` constraint on the pager container to prevent layout exceptions.

### What we will improve in the next sprint
- Add offline caching for hotel search results so the app remains usable when the API is temporarily unavailable.
- Improve error messaging when the booking API call fails mid-flow, distinguishing network errors from server-side rejections.
- Introduce more granular UI states (Loading, Empty, Error, Success) in the BookingViewModel to give the user clearer feedback.

---

## 5. Team Self-Assessment (0-10)

**Score:** 10

**Justification:** All planned tasks for the remote persistence and image gallery sprint were fully implemented. Retrofit is correctly integrated following the MVVM and repository patterns, the booking flow persists data in Room and calls the API as required, the image gallery is functional with camera and gallery support, and the reservation management screen covers listing, visual display and cancellation. Unit tests cover the repository, ViewModel and DAO layers with mocked responses.

---

## 6. Test Results

### BookingTest

| Test                                          | Result |
|-----------------------------------------------|--------|
| `getAvailable success returns list of hotels` | ✅ Pass |
| `getAvailable failure returns null`           | ✅ Pass |
| `bookRoom success returns BookingResponse`    | ✅ Pass |
| `bookRoom failure returns null`               | ✅ Pass |
| `cancelReservation success returns true`      | ✅ Pass |
| `cancelReservation failure returns false`     | ✅ Pass |
| `cancelReservation exception returns false`   | ✅ Pass |

### BookingViewModelTest

| Test                                                       | Result |
|------------------------------------------------------------|--------|
| `getAvailable success updates hotels and uiState`          | ✅ Pass |
| `getAvailable failure sets uiState to Error`               | ✅ Pass |
| `getAvailable exception sets uiState to Error`             | ✅ Pass |
| `bookRoom success updates lastBookingResponse and uiState` | ✅ Pass |
| `bookRoom failure sets uiState to Error`                   | ✅ Pass |
| `selectHotel updates selectedHotel`                        | ✅ Pass |
| `selectRoom updates selectedRoom`                          | ✅ Pass |
| `resetState resets uiState to Idle`                        | ✅ Pass |

### TripViewModelTest

| Test                                                                 | Result |
|----------------------------------------------------------------------|--------|
| `trips sorted by date proximity ascending`                           | ✅ Pass |
| `trips with same proximity sorted by id descending`                  | ✅ Pass |
| `reservations returns only trips with non-null reservationId`        | ✅ Pass |
| `reservations is empty when no trips have reservationId`             | ✅ Pass |
| `addTripFromBooking stores all reservation fields`                   | ✅ Pass |
| `addTripFromBooking uses city drawable URI for Barcelona`            | ✅ Pass |
| `addTripFromBooking uses Paris drawable when city is Paris`          | ✅ Pass |
| `addTripFromBooking uses London drawable when city is London`        | ✅ Pass |
| `addTripFromBooking creates HOTEL itinerary item`                    | ✅ Pass |
| `deleteTrip with reservationId cancels via API then deletes locally` | ✅ Pass |
| `deleteTrip without reservationId skips API call`                    | ✅ Pass |
| `nextTrip returns closest future trip`                               | ✅ Pass |

### ImageDaoTest

| Test                                                    | Result |
|---------------------------------------------------------|--------|
| `insertImage should add image to trip`                  | ✅ Pass |
| `getImagesByTrip should return only that trip's images` | ✅ Pass |
| `deleteImage should remove specific image`              | ✅ Pass |
| `deleteTrip should cascade delete its images`           | ✅ Pass |

### Fixes applied during testing

- `BookingRepositoryImpl.getAvailable` initially did not handle a null response body on a successful HTTP status, causing a `NullPointerException`. Fixed by checking `response.body()` explicitly before accessing `available_hotels`.
- `ReservationCard` initially crashed when `trip.hotelImageUrl` was null because the URL was built unconditionally. Fixed by using a null-safe Coil model so the error placeholder is shown instead.
- The cascade delete of itinerary items when cancelling a reservation trip was already covered by the `ForeignKey.CASCADE` constraint on `ItineraryItem`, verified through `ImageDaoTest.deleteTrip_shouldCascadeDeleteItsImages` and the existing `ItineraryDaoTest.deleteTrip_shouldCascadeDeleteItsItems`.