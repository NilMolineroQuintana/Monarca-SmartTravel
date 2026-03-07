# Contributing to Monarca SmartTravel

This guide outlines the workflow and standards for the development team to ensure code quality and UI consistency.

## 🛠 Workflow

### Git Strategy
- **Initial Approach:** Development began using separate branches for different screens to work in parallel.
- **Consolidation:** To facilitate the sharing of custom UI components (like `WideOption` or `TripCard`) and ensure visual consistency, we decided to merge our work into the `main` branch. This allowed us to unify the component library and avoid duplication.
- **Stability:** We follow a "test before push" policy. All changes are committed locally and verified to be fully functional before being pushed to the remote `main` branch to prevent breaking the build.
- **Future Sprints** We would use the branch technique again in next sprints

### Commit Guidelines
- Use descriptive commit messages.
- Perform frequent local commits to track progress before the final push.

## 🎨 UI & Coding Standards
- **UI:** The app is built using Jetpack Compose, Material 3 and custom components.
- **Reusability:** Before creating a new UI element, check if it can be built using existing components in the `ui` package (e.g., `WideOption`).
- **Theming:** Ensure all components support both **Light** and **Dark** modes by using `MaterialTheme.colorScheme`.
- **Naming:** Follow standard Kotlin naming conventions (CamelCase for classes/composables, camelCase for variables).