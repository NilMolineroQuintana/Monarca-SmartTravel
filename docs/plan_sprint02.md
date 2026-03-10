# Sprint 02 – Planning Document

## 1. Sprint Goal
The main objective of Sprint 02 is to implement the core logic of the Travel Planner application. This includes managing trip itineraries, handling CRUD operations for travel items, persisting user preferences using SharedPreferences, ensuring proper input validation, implementing multi-language support, and writing tests to verify the correctness of the implemented logic.

---

## 2. Sprint Backlog

|   ID   | Task                                                                                                                                                  | Person in charge | Estimation (h) | Priority |
|:------:|-------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------:|:--------------:|:--------:|
|  T1.1  | Implement inMemory CRUD operations for trips (addTrip, editTrip, deleteTrip) following MVVM pattern                                                   |   TO ASSIGN   |       4        |   High   |
|  T1.2  | Implement inMemory CRUD operations for itinerary/activity items (addActivity, updateActivity, deleteActivity) following MVVM pattern                  |   TO ASSIGN   |       4        |   High   |
|  T1.3  | Ensure proper data validation (future dates, required fields, DatePickers in all date fields)                                                         |   TO ASSIGN   |       3        |   High   |
|  T1.4  | Implement user settings persistence using SharedPreferences (username, date of birth, dark mode, language)                                            |   TO ASSIGN   |       3        |   High   |
|  T1.5  | Implement multi-language support (minimum 3 languages: ca, es, en)                                                                                    |   TO ASSIGN   |       2        |   High   |
|  T2.1  | Structure the itinerary interaction flow: Menu → Travel → Itinerary (CRUD)                                                                            |   TO ASSIGN   |       2        |   High   |
|  T2.2  | Implement a basic UI flow for adding and modifying trip details and itinerary                                                                         |   TO ASSIGN   |       3        |   High   |
|  T2.3  | Ensure updates reflect dynamically in the main trip list and itinerary list                                                                           |   TO ASSIGN   |       2        |  Medium  |
|  T3.1  | Implement basic input validation (empty fields, incorrect dates, activities outside trip range) in ViewModel and UI layers                            |   Guillem & Nil   |       3        |   High   |
|  T3.2  | Write unit tests for trip and itinerary CRUD operations                                                                                               |   Guillem & Nil   |       3        |   High   |
|  T3.3  | Simulate user interactions and log errors or unexpected behaviors                                                                                     |   Guillem & Nil   |       2        |  Medium  |
|  T3.4  | Update documentation with test results and fixes applied                                                                                              |   Guillem & Nil   |       1        |   Low    |
|  T3.5  | Add logs (visible in Logcat) and comments applying good practices                                                                                     |   Guillem & Nil   |       2        |  Medium  |

---

## 3. Definition of Done (DoD)
- [ ] inMemory CRUD operations for trips implemented following MVVM
- [ ] inMemory CRUD operations for itinerary activities implemented following MVVM
- [ ] Data validation implemented in ViewModel and UI layers (DatePickers, required fields, date ranges)
- [ ] User preferences persisted with SharedPreferences and loaded on app start
- [ ] Multi-language support implemented (ca, es, en)
- [ ] Unit tests written for all major CRUD operations
- [ ] Logs added to Logcat for CRUD operations, validation errors and unexpected behaviors
- [ ] Documentation updated with test results
- [ ] Release v2.X.X published on GitHub repository

---

## 4. Identified Risks

- Loss of ViewModel state on screen rotation or navigation between screens.
- Incorrect or missing date validation: trip start date must be before end date, and activities must be within the trip date range.
- Not showing clear error messages to the user when a validation rule is violated (silent failures).
- Storing user preferences only in memory and not reloading them when the app restarts.
- Accessing the data source directly from the UI, bypassing the ViewModel and Repository layers.
- Missing or insufficient Logcat logging, making debugging and evaluation harder.