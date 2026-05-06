# Sprint 04 – Planning Document

## 1. Sprint Goal
The main objective of Sprint 04 is to integrate remote persistence by connecting the app to an external REST API using Retrofit. This includes implementing hotel search, room booking with local persistence, reservation listing and cancellation, and hotel image display. In parallel, the sprint delivers the image gallery feature per trip, allowing users to attach, store and browse photos locally for each trip.

---

## 2. Sprint Backlog

|    ID     | Task                                                                                                                               | Person in charge  | Estimation (h) | Priority |
|:---------:|------------------------------------------------------------------------------------------------------------------------------------|:-----------------:|:--------------:|:--------:|
|  **T1**   | **Retrofit Configuration**                                                                                                         |                   |                |          |
|   T1.1    | Add Retrofit dependencies; configure the HTTP client with base URL and converters                                        |   Guillem & Nil   |       1        |   High   |
|   T1.2    | Create remote data models and a Retrofit API interface with all required endpoints following the MVVM structure                     |   Guillem & Nil   |       2        |   High   |
|   T1.3    | Create a repository interface and its implementation to abstract the API calls following the repository pattern                     |   Guillem & Nil   |      1.5       |   High   |
|   T1.4    | Write unit tests for the repository layer mocking the remote Retrofit connection                                                    |   Guillem & Nil   |       2        |   High   |
|  **T2**   | **Search and Booking Screens**                                                                                                     |                   |                |          |
|   T2.1    | Create a hotel search screen allowing the user to select a city (London, Paris or Barcelona) and pick start and end dates          |        Nil        |       2        |   High   |
|   T2.2    | Display the list of hotels and their rooms returned by the API          |        Nil        |       3        |   High   |
|   T2.3    | Implement booking flow: allow the user to select a room and save the reservation info locally as a trip                            |      Guillem      |       3        |   High   |
|   T2.4    | Display all hotel and room images on the hotel detail and booking screen                                                            |      Guillem      |      1.5       |   High   |
|  **T3**   | **Add Images / Gallery to Trip**                                                                                                   |                   |                |          |
|   T3.1    | Allow the user to attach multiple images to a trip from the device camera or gallery using an image picker                         |        Nil        |       2        |  Medium  |
|   T3.2    | Save the selected images locally and link them to the corresponding trip                                                            |        Nil        |      1.5       |  Medium  |
|   T3.3    | Display the trip-specific photo gallery in the album screen, replacing the current mock data                                        |        Nil        |       1        |  Medium  |
|  **T4**   | **List and Cancel Reservations**                                                                                                   |                   |                |          |
|   T4.1    | Create a reservations screen listing all locally stored hotel reservations with their associated trip information                   |      Guillem      |       2        |   High   |
|   T4.2    | Implement reservation cancellation: delete the reservation locally and call the API cancellation endpoint if required              |      Guillem      |      1.5       |   High   |
|   T4.3    | Show hotel and room images in the reservation list, following the reference implementation style                                    |      Guillem      |       1        |  Medium  |
|   T4.4    | Update the trips screen and trip card to indicate visually when a trip has an associated hotel reservation                          |   Guillem & Nil   |       1        |  Medium  |

---

## 3. Definition of Done (DoD)
- Retrofit configured with the hotel API base URL
- Remote data models and API interface created and mapped to the hotel API endpoints
- Hotel repository interface and implementation following the repository pattern wired through Hilt
- Unit tests written for the repository layer using Retrofit mocks
- Hotel search screen functional: city selector + date pickers + API call on submit
- Hotel and room list displayed with images fetched from the remote API
- Room booking saves reservation data locally in Room and navigates to confirmation
- Hotel and room images shown in the booking detail screen
- Image gallery feature implemented: users can attach images to a trip, stored locally and displayed in the album screen
- Reservations screen lists all local reservations with hotel and room images
- Reservation cancellation working both locally and via API
- Trip cards visually distinguish trips with hotel reservations
- Logcat logging added for all API calls, image operations and reservation events
- Documentation updated in `final_sprint04.md` with test results
- Release v4.x.x published on GitHub repository
- Evidence video saved under `/docs/evidence/v4.x.x`

---

## 4. Identified Risks

- The hotel API may be temporarily unavailable or change its contract during development; the repository layer must handle network errors gracefully and propagate typed error codes to the UI.
- Loading remote images without proper caching could cause performance issues on slower connections; the image loading library must be configured with memory and disk caches.
- Storing binary image data for the trip gallery can bloat the database; saving file paths to internal storage instead of raw bytes is the preferred approach.
- Integrating a new Retrofit-backed repository into Hilt alongside the existing Room repositories increases DI complexity; missing bindings cause hard-to-diagnose crashes at startup.
- The booking flow touches both the remote API and the local database in sequence; a failure midway must be handled with proper error recovery to avoid inconsistent state.
- Unit testing a Retrofit layer requires mocking the HTTP client or using a mock web server; unfamiliarity with this setup may slow down the testing tasks.