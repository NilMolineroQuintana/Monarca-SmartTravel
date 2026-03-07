# 📐 Diseño Arquitectónico de Monarca Smart Travel

## 🏛️ Arquitectura General

Monarca Smart Travel segueix una arquitectura **MVVM (Model-View-ViewModel)** per a una millor separació de responsabilitats i escalabilitat.

- **`ui/`** — Conté totes les pantalles i components visuals (View). Construïts íntegrament amb **Jetpack Compose** i el sistema de disseny **Material 3**, amb suport per a mode clar i fosc. Els components reutilitzables com `MyTopBar`, `MyBottomBar`, `TripCard` o `PopUp` es centralitzen a `MainLayout.kt`.
- **`domain/`** — Conté els models de dades i la lògica de negoci (Model). Aquesta capa és completament independent de frameworks, la qual cosa facilita els tests i la futura integració amb repositoris reals.

La navegació es gestiona amb **Jetpack Navigation Compose** des de `MainActivity`, amb un únic `NavHost` que centralitza totes les rutes. Les pantalles principals s'agrupen en tres seccions accessibles des de la barra inferior: **Inici**, **Viatges** i **Preferències**.

## 📊 Model de Dades: Creat complet per a futurs Sprints

El model de domini s'ha dissenyat pensant en l'escalabilitat. Totes les classes inclouen les funcions de negoci amb `@TODO` per implementar en sprints posteriors, seguint el contracte definit des del primer sprint.

Les decisions principals del model són:
- **`User` i `Authentication` separats** — per mantenir les dades de perfil separades de les credencials sensibles (SRP).
- **`ItineraryItem` unificat** — una sola classe amb camps opcionals cobreix tots els tipus de pla (transport i allotjament), simplificant la persistència futura.
- **`PlanType` com a enum amb metadades** — cada tipus de pla porta associada la seva icona, colors i ruta, evitant taules de correspondències disperses a la UI.
- **`AIRecommendations`** — preparat des del Sprint 01 per anticipar la funcionalitat d'IA del producte.

![Domain Model](domain-model.png)