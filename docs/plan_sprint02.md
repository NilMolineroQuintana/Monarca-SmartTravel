# Sprint 02 – Planning Document

## 1. Sprint Goal
The main objective of Sprint 02 is to implement the core logic of the Travel Planner application. This includes managing trip itineraries, handling CRUD operations for travel items, persisting user preferences using SharedPreferences, ensuring proper input validation, implementing multi-language support, and writing tests to verify the correctness of the implemented logic.

---

## 2. Sprint Backlog

|    ID     | Task                                                                                                         | Person in charge  | Estimation (h) | Priority |
|:---------:|--------------------------------------------------------------------------------------------------------------|:-----------------:|:--------------:|:--------:|
|  **T1**   | **Data & Persistence**                                                                                       |                   |                |          |
| *T1-Trip* | *Trips*                                                                                                      |                   |                |          |
|   T1.1.1  | Implement inMemory **addTrip** following MVVM pattern                                                        |      Guillem      |      1.5       |   High   |
|   T1.1.2  | Implement inMemory **editTrip** following MVVM pattern                                                       |      Guillem      |      1.5       |   High   |
|   T1.1.3  | Implement inMemory **deleteTrip** following MVVM pattern                                                     |      Guillem      |       1        |   High   |
| *T1-Itin* | *Itinerary / Activity*                                                                                       |                   |                |          |
|   T1.2.1  | Implement inMemory **addActivity** following MVVM pattern                                                    |        Nil        |      1.5       |   High   |
|   T1.2.2  | Implement inMemory **updateActivity** following MVVM pattern                                                 |        Nil        |      1.5       |   High   |
|   T1.2.3  | Implement inMemory **deleteActivity** following MVVM pattern                                                 |        Nil        |       1        |   High   |
|   T1.3    | Ensure proper data validation (future dates, required fields, DatePickers in all date fields)                |      Guillem & Nil      |       3        |   High   |
|   T1.4    | Implement user settings persistence using SharedPreferences (username, date of birth, dark mode, language)   |        Nil        |       2        |   High   |
|   T1.5    | Implement multi-language support (minimum 3 languages: ca, es, en)                                           |        Nil        |       2        |   High   |
|  **T2**   | **UI & Navigation**                                                                                          |                   |                |          |
|   T2.1    | Structure the itinerary interaction flow: Menu → Travel → Itinerary (CRUD)                                   |        Nil        |       2        |   High   |
| *T2-Trip* | *Trips*                                                                                                      |                   |                |          |
|   T2.2.1  | Implement UI flow for **adding and modifying trip** details                                                  |   Guillem & Nil   |      1.5       |   High   |
|   T2.3.1  | Ensure updates reflect dynamically in the **main trip list**                                                 |   Guillem & Nil   |       1        |  Medium  |
| *T2-Itin* | *Itinerary / Activity*                                                                                       |                   |                |          |
|   T2.2.2  | Implement UI flow for **adding and modifying itinerary** details                                             |   Guillem & Nil   |      1.5       |   High   |
|   T2.3.2  | Ensure updates reflect dynamically in the **itinerary list**                                                 |   Guillem & Nil   |       1        |  Medium  |
|  **T3**   | **Testing & Quality**                                                                                        |                   |                |          |
|   T3.1    | Implement basic input validation (empty fields, incorrect dates, activities outside trip range)               |   Guillem & Nil   |       3        |   High   |
| *T3-Trip* | *Trips*                                                                                                      |                   |                |          |
|   T3.2.1  | Write unit tests for **trip** CRUD operations                                                                |   Guillem & Nil   |      1.5       |   High   |
| *T3-Itin* | *Itinerary / Activity*                                                                                       |                   |                |          |
|   T3.2.2  | Write unit tests for **itinerary** CRUD operations                                                           |   Guillem & Nil   |      1.5       |   High   |
|   T3.3    | Simulate user interactions and log errors or unexpected behaviors                                            |   Guillem & Nil   |       2        |  Medium  |
|   T3.4    | Update documentation with test results and fixes applied                                                     |      Guillem      |       1        |   Low    |
|   T3.5    | Add logs (visible in Logcat) and comments applying good practices                                            |   Guillem & Nil   |       2        |  Medium  |

---

## 3. Definition of Done (DoD)
- Implement base MVVM structure
- inMemory CRUD operations for trips implemented following MVVM
- inMemory CRUD operations for itinerary activities implemented following MVVM
- Data validation implemented in ViewModel and UI layers (DatePickers, required fields, date ranges)
- User preferences persisted with SharedPreferences and loaded on app start
- Multi-language support implemented (ca, es, en)
- Unit tests written for all major CRUD operations
- Logs added to Logcat for CRUD operations, validation errors and unexpected behaviors
- Documentation updated with test results
- Release v2.X.X published on GitHub repository

---

## 4. Identified Risks

- Loss of ViewModel state on screen rotation or navigation between screens.
- Incorrect or missing date validation: trip start date must be before end date, and activities must be within the trip date range.
- Not showing clear error messages to the user when a validation rule is violated (silent failures).
- Storing user preferences only in memory and not reloading them when the app restarts.
- Accessing the data source directly from the UI, bypassing the ViewModel and Repository layers.
- Missing or insufficient Logcat logging, making debugging and evaluation harder.