# ⚽ Dicosport by Arafin

> **Broadcast your sports news with modern Material 3 and Clean Architecture!**

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-blue.svg)
![Theme](https://img.shields.io/badge/Theme-Neon%20Green-success.svg)

---

## 📱 About Dicosport
**Dicosport by Arafin** is a cutting-edge mobile sports news broadcasting application built with pure **XML Views**, **Fragments**, and **Jetpack Compose / Material 3 Guidelines**. It connects to **TheSportsDB API** to deliver live English Premier League (EPL) team news, stats, and match updates with smooth transitions and an immersive reading experience.

---

## 🌟 Key Features & Architecture

- **Clean Architecture**: Strictly separated into Presentation, Domain, and Data layers across multi-modules (`:app`, `:core`, `:favourite`).
- **Offline-First with Room**: Robust local database caching ensuring seamless offline access to sports news and saved favourites.
- **Dependency Injection**: Powered by **Koin** for efficient and modular service location.
- **Reactive Programming**: Built using Kotlin Coroutines and Flows for non-blocking asynchronous operations.
- **Dynamic Feature Module**: Favourite screen is packaged as a dynamic feature (`:favourite`).
- **Modern UI & UX**:
  - Material 3 Design with custom **Neon Green** accent (`#00FF66`).
  - ViewPager2 featured carousel & RecyclerView lists.
  - Search bar with real-time filtering.
  - Smooth zoom-in bookmark animation and accessibility title talkback.
  - Navigation Drawer supporting Favourite navigation, language switching (English, Bahasa Indonesia, Español), Dark/Light mode toggle, and exit confirmation dialog.
  - Localized error states with bilingual messages and retry mechanism.

---

## 🛠️ Tech Stack

- **UI**: XML Views, ViewBinding, ViewPager2, Material Components (Material 3)
- **Architecture**: MVVM + Clean Architecture (Presentation, Domain, Data)
- **Concurrency**: Kotlin Coroutines & Flow
- **Network**: Retrofit, Moshi Converter, OkHttp Logging Interceptor
- **Database**: Room Persistence Library
- **DI**: Koin
- **Image Loading**: Glide with size optimization (`/medium` preview suffix)
- **Modularity**: Android Library (`:core`) & Dynamic Feature (`:favourite`)
