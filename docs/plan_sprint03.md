# Sprint 03 – Planning Document

## 1. Sprint Goal
The main objective of Sprint 03 is to replace the in-memory fake data sources from Sprint 02 with a persistent SQLite database using Room (entities, DAOs, migrations). In parallel, the sprint introduces Firebase Authentication to handle real login, registration, and password recovery flows, and adds a local user table that ties trip data to authenticated users. All new database interactions must be tested, logged, and documented.

---

## 2. Sprint Backlog

|    ID     | Task                                                                                                                    | Person in charge  | Estimation (h) | Priority |
|:---------:|-------------------------------------------------------------------------------------------------------------------------|:-----------------:|:--------------:|:--------:|
|  **T1**   | **Implement SQLite Persistence using Room**                                                                             |                   |                |          |
|   T1.1    | Create Room Database class (`MonarcaDatabase`)                                                                          |   Guillem & Nil   |       1        |   High   |
|   T1.2    | Define Room Entities for `Trip` and `ItineraryItem` (must include datetime, text and integer fields)                    |   Guillem & Nil   |       2        |   High   |
|   T1.3    | Create Data Access Objects (DAOs) for Trip and ItineraryItem with all required query methods                            |   Guillem & Nil   |       2        |   High   |
|   T1.4    | Implement CRUD operations through DAOs for both trips and itinerary items                                               |   Guillem & Nil   |       3        |   High   |
|   T1.5    | Modify `TripViewModel` and `ItineraryViewModel` to use Room instead of in-memory fake data sources                      |   Guillem & Nil   |       3        |   High   |
|   T1.6    | Ensure UI recomposes correctly when database state changes (Flow / StateFlow integration)                                |   Guillem & Nil   |       1        |   High   |
|  **T2**   | **Login and Logout**                                                                                                    |                   |                |          |
|   T2.1    | Connect the app to Firebase (add `google-services.json`, configure dependencies)                                        |      Guillem      |       1        |   High   |
|   T2.2    | Design the login screen (email + password form, validation)                                                             |      Guillem      |       1        |   High   |
|   T2.3    | Implement Firebase email/password authentication for login; redirect to main view on success                            |      Guillem      |       2        |   High   |
|   T2.4    | Create logout action accessible from the preferences screen; redirect to login on logout                                |      Guillem      |      0.5       |   High   |
|   T2.5    | Add Logcat logging for all authentication operations and errors                                                          |      Guillem      |      0.5       |  Medium  |
|  **T3**   | **Register and Recover password**                                                                                       |                   |                |          |
|   T3.1    | Design the registration screen (sign-up form with required fields)                                                      |        Nil        |       1        |   High   |
|   T3.2    | Implement Firebase registration (email/password) using repository pattern; include email verification                    |        Nil        |       2        |   High   |
|   T3.3    | Implement password recovery screen and action (Firebase `sendPasswordResetEmail`)                                        |        Nil        |      1.5       |  Medium  |
|  **T4**   | **Persist user information and trip**                                                                                   |                   |                |          |
|   T4.1    | Create local `User` table in Room (login, username, birthdate, address, country, phone, accept emails); check username uniqueness |        Nil        |       2        |   High   |
|   T4.2    | Update `Trip` table to support multiple users; filter trips by the currently authenticated user                          |      Guillem      |      1.5       |   High   |
|   T4.3    | Update `design.md` with the full database schema and migration strategy                                                  |   Guillem & Nil   |       1        |  Medium  |
|   T4.4    | Create an access-log table that persists every login and logout event (userId + datetime)                                |        Nil        |      1.5       |  Medium  |
|  **T5**   | **Testing and Debugging**                                                                                               |                   |                |          |
|   T5.1    | Write unit tests for DAOs and database interactions (Trip DAO → Guillem, ItineraryItem DAO → Nil)                       |   Guillem & Nil   |       3        |   High   |
|   T5.2    | Implement data validation: prevent duplicate trip names, check valid dates, enforce required fields                      |   Guillem & Nil   |       1        |   High   |
|   T5.3    | Add Logcat logging at all database operations and error paths in repositories and ViewModels                             |   Guillem & Nil   |       1        |  Medium  |
|   T5.4    | Update documentation with test results and fixes applied                                                                 |      Guillem & Nil      |      0.5       |   Low    |

---

## 3. Definition of Done (DoD)
- Room Database class created and wired through Hilt
- `Trip` and `ItineraryItem` Room Entities defined with all required field types
- DAOs implemented with all CRUD query methods
- `TripViewModel` and `ItineraryViewModel` fully migrated off fake data sources; no `FakeTripDataSource` or `FakeItineraryItemDataSource` used at runtime
- UI recomposes reactively from Room via `Flow` / `StateFlow`
- Firebase project connected; login and logout working end-to-end
- Registration with email verification implemented using repository pattern
- Password recovery screen implemented
- Local `User` table created; username uniqueness enforced
- Trip table scoped to the authenticated user
- Access-log table persisting all login/logout events
- Unit tests written for all DAOs
- Logcat logging in place for all DB and auth operations
- `design.md` updated with full database schema
- Release v3.x.x published on GitHub repository
- Evidence video saved under `/docs/evidence/v3.x.x`

---

## 4. Identified Risks

- Room type converters for `Date` fields must be defined explicitly; forgetting them causes a compile-time crash that can be hard to diagnose for the first time.
- Migrating ViewModels from synchronous in-memory calls to `suspend` / `Flow`-based Room calls requires touching many layers at once — high risk of breaking existing functionality.
- Repository interface functions that are not yet `suspend` will need to be updated before Room integration; doing this incrementally reduces regression risk.
- Email verification adds an async step to the registration flow that must be handled gracefully in the UI (e.g., "check your inbox" state) to avoid user confusion.
- Scoping trips to the authenticated user requires coordinating Firebase UID with the local Room `User` table, which involves a join that has not existed in the codebase before.
- Test isolation for Room requires in-memory database instances (`Room.inMemoryDatabaseBuilder`); reusing the singleton database across tests will cause the same cross-test contamination we resolved with `reset()` in Sprint 02.