# 📐 Architectural Design — Monarca Smart Travel

## 🏛️ General Architecture

Monarca Smart Travel follows an **MVVM (Model-View-ViewModel)** architecture for better separation of concerns and scalability.

- **`ui/`** — Contains all screens and visual components (View). Built entirely with **Jetpack Compose** and the **Material 3** design system, with support for both light and dark mode. Reusable components such as `MyTopBar`, `MyBottomBar`, `TripCard` and `PopUp` are centralised in `MainLayout.kt`.
- **`domain/`** — Contains data models and business logic (Model). This layer is completely framework-independent, which facilitates testing and future integration with real repositories.

Navigation is managed with **Jetpack Navigation Compose** from `MainActivity`, using a single `NavHost` that centralises all routes. The main screens are grouped into three sections accessible from the bottom navigation bar: **Home**, **Trips** and **Preferences**.

## 📊 Data Model

The key design decisions are:
- **Expanded `User` profile and `AccesHistory` Auditing** — The `Authentication` class has been removed and its fields unified directly within the `User` class, which now holds a much richer profile (`username`, `birthdate`, `phoneNum`, `country`, `address`, and `recieveEmails`). A new `AccesHistory` entity has been introduced to keep an auditable, timestamped log of user actions.
- **Flexible `Trip` structure** — The concept of a trip has been broadened. Instead of a single `destination` string, trips now use a `title` and a `description`, allowing for better customisation. Trips can also be linked to a hotel reservation via the optional fields `reservationId`, `hotelImageUrl`, `roomType` and `hotelName`.
- **Unified `ItineraryItem`** — a single class with optional fields covers all plan types (transport and accommodation), simplifying persistence. The `isFromReservation` flag marks items generated automatically from a hotel booking, preventing manual editing or deletion.
- **`PlanType` as an enum with metadata** — each plan type carries its icon, colours and route, avoiding scattered mapping tables in the UI.
- **Remote hotel models** — `Hotel`, `Room`, `BookingData` and `BookingResponse` represent the data contract with the external REST API. These are plain data classes used only in the network and repository layers and are not persisted in Room.

![Domain Model](domain-model.png)