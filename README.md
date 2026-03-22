# FareSplitter 🚗

A simple Android app to help track shared ride expenses with friends (Uber, 99 Taxi, etc.).

Built as a personal learning project in Kotlin — my mom needed a way to keep track of who owes what when sharing rides with friends.

> App icon by [Freepik](https://www.freepik.com/icon/ridesharing_18317198)

## Screenshots

<!-- Add screenshots here -->
| Home | Add Ride | Summary |
|---|---|---|
| ![Home](screenshots/home.png) | ![Add Ride](screenshots/add_ride.png) | ![Summary](screenshots/summary.png) |

---

## Features

- Add friends you share rides with
- Log rides — app, fare, date and who was in the car
- Live split preview as you fill in the form
- Monthly summary showing how much each friend owes you
- Export via WhatsApp or save as a `.txt` file

---

## Tech Stack

- Kotlin 2.3.0, Min SDK 29 (Android 10)
- Android Views + Activities (no Compose)
- MVVM — ViewModel + LiveData + Room 2.7.1
- No dependency injection framework

---

## Architecture

```
Activity → ViewModel → RideRepository → Room DAOs
```

---

## How the Split Works

```
each person pays = totalFare / numberOfParticipants
```

The monthly summary adds up every share per friend across all rides in the month.

---

## Running

1. Clone and open in Android Studio (Hedgehog+)
2. Gradle sync
3. Run on API 29+ device or emulator

No API keys needed — fully local.

---

## Roadmap

- [ ] Monthly filtering in Summary screen
- [ ] Delete confirmation dialogs
- [ ] Edit a ride after saving