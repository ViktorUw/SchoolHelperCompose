# SchoolHelperCompose

The **Jetpack Compose** version of [SchoolHelper](https://github.com/ViktorUw/SchoolHelper): an Android app that shows assignment lists for each subject and the average grade per subject.

## Features

- **Task lists** screen with the subject, number of tasks and grade for each list
- Tap a list to open its tasks with content and points
- **Grades** screen with the average grade for each subject
- Bottom navigation bar (Material 3 `NavigationBar`)

> The app uses generated sample data, so it works without a backend.

## Tech stack

- **Kotlin**
- **Jetpack Compose** with Material 3
- **Navigation Compose** (routes with arguments, e.g. `przedmiot/{listId}`)
- `LazyColumn` lists
- Min SDK 28, target SDK 35

## Project structure

```
app/src/main/java/com/example/schoolhelpercompose/
├── MainActivity.kt          # Screens, navigation graph and bottom bar
├── Subject.kt, Exercise.kt, ExerciseList.kt  # Models
├── DataGenerator.kt         # Sample data and average calculation
└── ui/theme/                # Colors, typography and theme
```

## Getting started

1. Clone the repository:
   ```bash
   git clone https://github.com/ViktorUw/SchoolHelperCompose.git
   ```
2. Open the project in **Android Studio** and let Gradle sync.
3. Run the app on an emulator or a device with Android 9.0 (API 28) or newer.

> The interface is in Polish.
