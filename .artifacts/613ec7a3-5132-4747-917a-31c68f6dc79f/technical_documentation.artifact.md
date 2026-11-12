# Technical Documentation - Dicosport by Arafin

Welcome to the official technical documentation for **Dicosport by Arafin**, a modern Android sports news broadcasting application built with pure XML Views, Clean Architecture, Material 3 guidelines, and Jetpack components.

---

## 1. Architectural Design & Multi-Module Structure

Dicosport strictly adheres to **Clean Architecture** principles, enforcing separation of concerns across three distinct layers distributed across three modules:

```mermaid
graph TD
    subgraph :app (Presentation & DI)
        HomeFragment --> HomeViewModel
        DetailActivity --> DetailViewModel
        MainActivity --> NavHostFragment
    end

    subgraph :core (Domain & Data)
        HomeViewModel --> GetEplNewsUseCase
        DetailViewModel --> SetFavouriteNewsUseCase
        GetEplNewsUseCase --> NewsRepository
        NewsRepository --> ApiService
        NewsRepository --> NewsDao
    end

    subgraph :favourite (Dynamic Feature)
        FavouriteActivity --> FavouriteFragment
        FavouriteFragment --> FavouriteViewModel
        FavouriteViewModel --> GetFavouriteNewsUseCase
    end
```

- **Domain Layer (`:core`)**: Pure Kotlin with zero Android framework dependencies. Contains business models (`NewsItem`), repository interfaces (`NewsRepository`), and use cases (`GetEplNewsUseCase`, `GetFavouriteNewsUseCase`, `SetFavouriteNewsUseCase`, `CheckFavouriteNewsUseCase`).
- **Data Layer (`:core`)**: Implements an **Offline-First** repository pattern combining Remote Data Source (`ApiService` via Retrofit + Moshi) and Local Data Source (`NewsDatabase` & `NewsDao` via Room).
- **Presentation Layer (`:app`, `:favourite`)**: UI components (Activities, Fragments, Adapters, ViewModels) observing reactive data streams using LiveData and Coroutines.

---

## 2. Core Technical Components

### A. Offline-First Caching (Room)
- **`NewsEntity`**: Database schema representing cached news items and saved favourites (`[NewsEntity.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/data/source/local/entity/NewsEntity.kt)`).
- **`NewsDao`**: Provides reactive queries (`Flow`) for all news and bookmarked favourites.
- **Strategy**: When fetching news, the repository emits local cache instantly, fetches fresh data from **TheSportsDB API**, updates the local cache, and emits the latest data, ensuring seamless offline usability.

### B. Networking & Image Pipeline (Retrofit + Moshi + Glide)
- **`ApiService`**: Connects to `https://www.thesportsdb.com/api/v1/json/123/` to fetch English Premier League team data (`search_all_teams.php`).
- **Image Optimization**: Automatically appends the `/medium` preview suffix to banner/fanart URLs per TheSportsDB specification while sanitizing all URLs to secure HTTPS.

### C. Dependency Injection (Koin) & Reactive Programming (Flow)
- **Koin**: Modular DI setup (`networkModule`, `databaseModule`, `repositoryModule`, `useCaseModule`, `appModule`, `favouriteModule`).
- **Flow & Debounce**: Search queries in `HomeViewModel` use `.debounce(300)` and `.distinctUntilChanged()` for optimized reactive filtering.

---

## 3. Step-by-Step Guide on How to Read the Codebase

Follow this step-by-step reading path to understand how the app works from end to end:

### Step 1: Application Entry Point & DI Initialization
- **File**: `[DicosportApp.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/java/com/example/dicosportbyarafin/DicosportApp.kt)`
- **What to look for**: See how Koin dependency injection is initialized on app startup, loading all core and feature modules. Also notice how the user's theme preference is restored from `SharedPreferences`.

### Step 2: Main Activity & Navigation Component
- **File**: `[MainActivity.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/java/com/example/dicosportbyarafin/MainActivity.kt)`
- **What to look for**: Observe how `NavHostFragment` (`[nav_graph.xml](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/res/navigation/nav_graph.xml)`) manages in-app base navigation, how `ActionBarDrawerToggle` powers the Navigation Drawer, and how language, theme dialogs, and dynamic feature intents (`FavouriteActivity`) are dispatched.

### Step 3: Domain & Business Logic (`:core`)
- **Files**:
  - `[NewsItem.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/domain/model/NewsItem.kt)` (Pure Kotlin business model)
  - `[NewsRepository.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/domain/repository/NewsRepository.kt)` (Repository contract)
  - `[GetEplNewsUseCase.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/domain/usecase/GetEplNewsUseCase.kt)` (Use case business logic)
- **What to look for**: Notice that the domain layer contains zero Android framework imports, keeping business rules pure and testable.

### Step 4: Data Layer & Offline-First Implementation
- **Files**:
  - `[NewsRepositoryImpl.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/data/NewsRepositoryImpl.kt)`
  - `[NewsDatabase.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/core/src/main/java/com/example/core/data/source/local/room/NewsDatabase.kt)`
- **What to look for**: Study how the repository coordinates between Retrofit (`ApiService`), Room (`NewsDao`), and Flow streams to deliver an offline-first experience.

### Step 5: Presentation Layer & UI Features
- **Files**:
  - `[HomeFragment.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/java/com/example/dicosportbyarafin/presentation/home/HomeFragment.kt)` & `[HomeViewModel.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/java/com/example/dicosportbyarafin/presentation/home/HomeViewModel.kt)`
  - `[DetailActivity.kt](file:///C:/Users/akmal/AndroidStudioProjects/DicosportbyArafin/app/src/main/java/com/example/dicosportbyarafin/presentation/detail/DetailActivity.kt)`
- **What to look for**: Check out the reactive search debounce, pull-to-refresh (`SwipeRefreshLayout`), modal loading indicators, bookmark animations, and tap-to-zoom full-screen image preview.
