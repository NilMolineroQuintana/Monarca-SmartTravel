# Sprint 01 – Execution & Review

## 1. Obtained results

### Comparison with Sprint Goal:

TODO

---

## 2. Tasks completed

|    ID     | Completed | Comments |
|:---------:|:---------:|----------|
|  **T1**   |           | **Data & Persistence** |
| *T1-Trip* |           | *Trips* |
|   T1.1.1  |    Yes    | Implemented following full MVVM flow: UI → TripViewModel → TripRepositoryImpl → FakeTripDataSource. |
|   T1.1.2  |    No    | |
|   T1.1.3  |    Yes    | `deleteTrip` implemented with confirmation PopUp and automatic navigation back to trips list. |
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
|   T3.2.1  |    No     | |
| *T3-Itin* |           | *Itinerary / Activity* |
|   T3.2.2  |    Yes    | `FakeItineraryItemDataSourceTest` covers add, get, update and delete. `ItineraryViewModelTest` covers date validation edge cases including boundary dates and out-of-range scenarios. |
|   T3.3    |    Yes    | Manual interaction testing performed across all screens. Unexpected behaviours logged via `Log.e` / `Log.w` in ViewModels and repositories. |
|   T3.4    |    No    |  |
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

**Score:** TODO

**Justification:** TODO