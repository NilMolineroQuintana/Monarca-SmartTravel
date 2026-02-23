# Sprint 01 – Planning Document

## 1. Sprint Goal

The main objective of Sprint 01 is to build the initial structure and skeleton of the Travel Planner app by setting up an Android Studio project, initializing a GitHub repository, designing the core screens with mocked data and navigation, documenting the project, implementing initial customization, and publishing the final submission as a GitHub Release.

---

## 2. Sprint Backlog

| ID | Task | Person in charge | Estimation (h) | Priority |
|:----:|-------|:-------------:|:----------------:|:-----------:|
| T1.1 | Create a Product Name | Guillem & Nil | 0.5 | High |
| T1.2 | Generate a logo for your app using AI | Guillem | 2 | High |
| T1.3 | Initialize the project using Android Studio defining API and Kotlin Versions | Nil | 0 | High |
| T2.1 | Finish data model diagram | Guillem & Nil | 2 | High |
| T2.2 | Document the data model diagram in [design.md](design.md) | Guillem & Nil | 1 | Medium |
| T3.1 | Develop the core screen layouts | Guillem & Nil | 10 | High |
| T3.1.1 | Develop the Home screen | Guillem | 2 | High |
| T3.1.2 | Develop the Trip screen | Guillem | 2 | High |
| T3.1.3 | Develop the Itinerary screen | Nil | 8 | High |
| T3.1.4 | Develop the User Preference screen | Nil | 4 | High |
| T3.1.5 | Develop the User Profile screen | Nil | 2 | High |
| T3.1.6 | Develop the About page | Guillem | 1 | High |
| T3.1.7 | Develop the Terms & Conditions screen | Nil | 0.5 | High |
| T3.1.8 | Develop the Splash screen with the app logo | Nil | 0.5 | Low |
| T3.1.9 | Develop the Log in screen with the app logo | Guillem | 0.5 | High |
| T3.2 | Implement navigation | Nil | 0.5 | High |
| T3.4 | Implement data model classes and their functions | Guillem & Nil | 14 | Medium |
| T3.4.1 | Implement User data class | Nil | 2 | Medium |
| T3.4.2 | Implement Preferences data class | Nil | 2 | Medium |
| T3.4.3 | Implement Authentication data class | Guillem | 2 | Medium |
| T3.4.4 | Implement Trip data class | Guillem | 2 | Medium |
| T3.4.5 | Implement ItineraryItem data class | Nil | 2 | Medium |
| T3.4.6 | Implement Image data class | Nil | 1 | Medium |
| T3.4.7 | Implement AIRecomendations data class | Guillem | 2 | Medium |
| T3.4.8 | Implement Map data class | Nil | 2 | Medium |

---

## 3. Definition of Done (DoD)

- [ ] Have implemented domain model 
- [ ] Have implemented all needed screens
- [ ] Theme applied to all screens
- [ ] README updated
- [ ] Release published

---

## 4. Identified risks

- Low experience in Android Studio + Jetpack Compose
- Improperly managing the navController can lead to a "circular" backstack, where pressing the back button traps the user in a loop between screens.¡
- Creating too many unique screens instead of reusable templates will make future UI changes twice as difficult to implement.

---
