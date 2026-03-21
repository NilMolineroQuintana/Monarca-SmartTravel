# Sprint 02 – Execution & Review

## 1. Obtained results

### Comparison with Sprint Goal:

The sprint goal was fully achieved. All planned CRUD operations for trips and itinerary items are implemented following the MVVM pattern, with validation at both UI and ViewModel layers. User preferences are persisted with SharedPreferences, multi-language support works across three languages, and unit tests cover all major operations. 

---

## 2. Tasks completed

|    ID     | Completed | Comments |
|:---------:|:---------:|----------|
|  **T1**   |           | **Data & Persistence** |
| *T1-Trip* |           | *Trips* |
|   T1.1.1  |    Yes    | Implemented following full MVVM flow: UI → TripViewModel → TripRepositoryImpl → FakeTripDataSource. |
|   T1.1.2  |    Yes    | Implemented as `updateTrip` in `TripViewModel` and `TripRepositoryImpl`. Includes itinerary range validation to ensure no existing items fall outside the new trip date range. |
|   T1.1.3  |    Yes    | `deleteTrip` implemented with cascade delete of all associated itinerary items, confirmation PopUp and automatic navigation back to trips list. |
| *T1-Itin* |           | *Itinerary / Activity* |
|   T1.2.1  |    Yes    | `addItem` implemented with full MVVM flow, including route-to-PlanType resolution. |
|   T1.2.2  |    Yes    | `updateItem` implemented, reusing `PlanScreen` with `itemId` parameter to pre-fill the form. |
|   T1.2.3  |    Yes    | `deleteItem` implemented with long-press triggering an `OptionsPopUp` and a confirmation `PopUp`. |
|   T1.3    |    Yes    | DatePickers used in all date fields blocking past dates. Required fields validated at both UI and ViewModel layers. Date range validated against trip bounds. |
|   T1.4    |    Yes    | `PreferencesManager` with SharedPreferences persists username, date of birth, dark mode, language and notifications. Values are loaded on app start via Hilt injection in `MainActivity`. |
|   T1.5    |    Yes    | Three languages supported (ca, es, en) via `LanguageChangeUtil`, with automatic device language detection as default. |
|  **T2**   |           | **UI & Navigation** |
|   T2.1    |    Yes    | Full flow implemented: BottomBar → TripsScreen → ItineraryScreen → PlanOptionsScreen → PlanScreen. |
| *T2-Trip* |           | *Trips* |
|   T2.2.1  |    Yes    | `CreateTripScreen` implemented with full form validation and Snackbar error feedback from the repository layer. |
|   T2.3.1  |    Yes    | `trips` state uses `mutableStateOf` in `TripViewModel`, so the list recomposes automatically after any CRUD operation. |
| *T2-Itin* |           | *Itinerary / Activity* |
|   T2.2.2  |    Yes    | `PlanScreen` adapts dynamically to all 6 plan types (transport and accommodation) and handles both add and edit modes. |
|   T2.3.2  |    Yes    | `_items` uses `StateFlow` collected with `collectAsState()`, so the itinerary recomposes after add, update or delete. |
|  **T3**   |           | **Testing & Quality** |
|   T3.1    |    Yes    | Empty fields, invalid price, incorrect date format, and activities outside trip range are all validated with specific `AppError` codes. |
| *T3-Trip* |           | *Trips* |
|   T3.2.1  |    Yes    | `FakeTripDataSourceTest` covers add, get, update and delete. `TripViewModelTest` covers addTrip, updateTrip and deleteTrip with valid and invalid inputs, including blank fields, invalid date ranges and non-existing IDs. |
| *T3-Itin* |           | *Itinerary / Activity* |
|   T3.2.2  |    Yes    | `FakeItineraryItemDataSourceTest` covers add, get, update and delete. `ItineraryViewModelTest` covers date validation edge cases including boundary dates and out-of-range scenarios. |
|   T3.3    |    Yes    | Manual interaction testing performed across all screens. Unexpected behaviours logged via `Log.e` / `Log.w` in ViewModels and repositories. |
|   T3.4    |    Yes    | Documentation updated in `final_sprint02.md` with all test results. |
|   T3.5    |    Yes    | Logcat logs added at all CRUD operations across `FakeTripDataSource`, `FakeItineraryItemDataSource`, `TripRepositoryImpl`, `ItineraryRepositoryImpl`, `TripViewModel` and `ItineraryViewModel`. KDoc comments added to all public functions and classes. |
---

## 3. Deviations

Move CRUD operations from domain model to ViewModel and Repository layers

Removed addTrip, editTrip and deleteTrip methods from the Trip domain model
and moved all CRUD logic to TripViewModel and TripRepositoryImpl, following
the MVVM pattern consistently. The domain model now acts as a pure data class.

The same pattern was applied to ItineraryItem: all add, update and delete
operations are handled exclusively through ItineraryViewModel and
ItineraryRepositoryImpl, with validation living in the ViewModel layer
and data access delegated to the Repository.

This ensures the UI never interacts with the data layer directly,
and that business logic is centralized and testable.

Cascade delete added to deleteTrip: when a trip is deleted, all associated
itinerary items are also removed automatically via TripRepositoryImpl,
ensuring data consistency without any extra user action.

---

## 4. Retrospective

### What worked well
- The MVVM pattern applied to itinerary CRUD worked cleanly: validation logic
  is centralized in `ItineraryViewModel`, data access is fully delegated to
  `ItineraryRepositoryImpl`, and the UI never touches the data layer directly.
- The `AppError` enum proved effective for propagating typed error codes from the
  data source up to the UI, avoiding silent failures.
- SharedPreferences persistence worked reliably: username, date of birth,
  dark mode and language are saved and correctly restored on app restart.
- Multi-language support (ca, es, en) integrates well with the system locale
  and falls back gracefully when the device language is not supported.
- Unit tests for both `FakeItineraryItemDataSource` and `ItineraryViewModel`
  covered all CRUD operations and edge cases, including boundary date validation.
- Trip CRUD unit tests (`FakeTripDataSourceTest` and `TripViewModelTest`) covered
  all operations with valid and invalid inputs, including blank fields and date range errors.

### What didn't work
- The unified `ItineraryItem` model with optional fields for transport vs.
  non-transport plans adds cognitive overhead when building and reading items,
  since it is not immediately clear which fields apply to each type.
- Date validation with minute-level precision required extra care to ensure
  boundary dates (exactly on trip start or end) were accepted correctly.

### What we will improve in the next sprint
- Replace the in-memory fake data sources with a real persistence layer.
- Implement real authentication instead of bypassing login directly to home.

---

## 5. Team Self-Assessment (0-10)

**Score:** 10

**Justification:** The sprint goal was fully achieved. All planned CRUD operations for trips and itinerary items are implemented following the MVVM pattern, with validation at both UI and ViewModel layers. User preferences are persisted with SharedPreferences, multi-language support works across three languages, and unit tests cover all major operations.

---

## 6. Test Results

### FakeTripDataSourceTest

| Test | Result |
|------|--------|
| `addTrip should add trip to list` | ✅ Pass |
| `getAllTrips should return all trips` | ✅ Pass |
| `getTripById should return correct trip` | ✅ Pass |
| `getTripById with invalid id should return null` | ✅ Pass |
| `updateTrip should modify existing trip` | ✅ Pass |
| `updateTrip with non-existing id should return NON_EXISTING_ITEM` | ✅ Pass |
| `deleteTrip should remove only that trip` | ✅ Pass |
| `deleteTrip with non-existing id should return NON_EXISTING_ITEM` | ✅ Pass |

### TripViewModelTest

| Test | Result |
|------|--------|
| `addTrip with valid data should succeed` | ✅ Pass |
| `addTrip with blank title should return INVALID_TITLE` | ✅ Pass |
| `addTrip with blank description should return INVALID_DESCRIPTION` | ✅ Pass |
| `addTrip with endDate before startDate should return INVALID_DATE_RANGE` | ✅ Pass |
| `addTrip with endDate equal to startDate should return INVALID_DATE_RANGE` | ✅ Pass |
| `updateTrip with valid data should succeed` | ✅ Pass |
| `updateTrip with blank title should return INVALID_TITLE` | ✅ Pass |
| `updateTrip with blank description should return INVALID_DESCRIPTION` | ✅ Pass |
| `updateTrip with invalid date range should return INVALID_DATE_RANGE` | ✅ Pass |
| `updateTrip with non-existing id should return false` | ✅ Pass |
| `deleteTrip should remove only that trip` | ✅ Pass |
| `deleteTrip with non-existing id should return false` | ✅ Pass |

### FakeItineraryItemDataSourceTest

| Test | Result |
|------|--------|
| `addItem should add activity to trip` | ✅ Pass |
| `getActivitiesByTrip should return only that trip's activities` | ✅ Pass |
| `getActivitiesByTrip with invalid tripId should return empty list` | ✅ Pass |
| `updateItem should modify existing activity` | ✅ Pass |
| `updateItem with non-existing id should return NON_EXISTING_ITEM` | ✅ Pass |
| `deleteItem should remove only that activity` | ✅ Pass |
| `deleteItem with non-existing id should return NON_EXISTING_ITEM` | ✅ Pass |

### ItineraryViewModelTest

| Test | Result |
|------|--------|
| `addItem within trip date range should succeed` | ✅ Pass |
| `addItem before trip startDate should return ITEM_OUT_OF_RANGE` | ✅ Pass |
| `addItem after trip endDate should return ITEM_OUT_OF_RANGE` | ✅ Pass |
| `addItem on exactly trip startDate should succeed` | ✅ Pass |
| `addItem on exactly trip endDate should succeed` | ✅ Pass |
| `addItem with invalid route should return UNKNOWN` | ✅ Pass |
| `addItem with invalid date format should return NON_EXISTING_DATE` | ✅ Pass |
| `updateItem with valid data should return OK` | ✅ Pass |
| `updateItem with non-existing id should return NON_EXISTING_ITEM` | ✅ Pass |
| `updateItem with invalid date format should return NON_EXISTING_DATE` | ✅ Pass |
| `updateItem with date out of trip range should return ITEM_OUT_OF_RANGE` | ✅ Pass |

### Fixes applied during testing

- Date validation was initially done using millisecond precision, which caused boundary dates (exactly on trip start or end) to be rejected. Fixed by clamping precision to minutes (`time / 60_000`) in both `ItineraryViewModel.validateDate` and `TripRepositoryImpl.validateItineraryItemsInRange`.
- `deleteTrip` initially left orphaned itinerary items in `FakeItineraryItemDataSource`. Fixed by adding cascade delete in `TripRepositoryImpl.deleteTrip`.