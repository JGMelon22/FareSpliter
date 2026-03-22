# FareSplitter 🚗

A simple Android app to help track shared ride expenses with friends (Uber, 99 Taxi, etc.).

Built as a personal learning project in Kotlin — my mom needed a way to keep track of who owes what when sharing rides with friends.

> App icon by [Freepik](https://www.freepik.com/icon/ridesharing_18317198)

## Screenshots

<!-- Add screenshots here -->
| Home | Add Friend | Add Ride | Summary |
|---|---|---|---|
| <img width="300" alt="Home" src="https://github.com/user-attachments/assets/607b8e79-c781-4260-89cf-09b73896d39d" /> | <img width="300" alt="Add Friend" src="https://github.com/user-attachments/assets/153d46f5-2d35-4d82-93ad-54ff0d3a9f9d" /> | <img width="300" alt="Add Ride" src="https://github.com/user-attachments/assets/376b8be6-02c0-4aff-8ac2-df7a66e55f06" /> | <img width="300" alt="Summary" src="https://github.com/user-attachments/assets/2517756d-406d-4377-bea9-23ef0f98cb6f" /> |

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

1. Clone and open in Android Studio 2025.2 (Panda+)
2. Gradle sync
3. Run on API 29+ device or emulator

---

## Roadmap

- [ ] Monthly filtering in Summary screen
- [ ] Delete confirmation dialogs
- [ ] Edit a ride after saving
