# ANDROID APP DEVELOPMENT COURSE
# GROUP: Julian Becaj, Kei Zavalani, Bruklin Hodaj
# SayDoneApp

SayDoneApp is a feature-rich Android application designed to help users efficiently manage their daily tasks and activities. The app leverages modern Android development best practices, providing a seamless and intuitive user experience for task organization, tracking, and completion.

## Project Description

SayDoneApp allows users to create, view, edit, and manage their personal tasks in a multi-activity environment. The app supports advanced task management functionalities and is built with a focus on user interface design, accessibility, and data persistence.

## Features and Tasks

- **Multi-Activity Structure**: The app is composed of several activities, each dedicated to a core function (e.g., Task List, Task Details).
- **Implicit Intents**: User can search for specific tasks in their notes.
- **User Interface Elements**:
  - **Checkboxes**: Mark tasks as completed.
  - **Other UI Components**: Floating action buttons, toolbars, and dialogs for improved interaction.
- **Lists**: The app displays tasks in a scrollable list using RecyclerView, allowing users to easily browse and manage their tasks.
- **Database Integration**: All tasks are saved persistently using an SQLite database (via Room). This ensures that user data is retained between sessions.
- **Network Communication**: This app connects to online and open APIs to generate random emojis.

## References

API: https://emojihub.yurace.pro/api/random  
GeeksForGeeks: https://www.geeksforgeeks.org/java-for-android-building-your-first-android-app/

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK (API level 21 or above)
- Internet connection for networking features

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/zavalani/SayDoneApp.git
   ```
2. Open the project in Android Studio.
3. Build and run the app on your preferred emulator or Android device.

### Usage

- Launch the app 
- Add new tasks using the floating action button.
- Mark tasks as complete with checkboxes.
- Search online for specific task
- Generate random emojis
