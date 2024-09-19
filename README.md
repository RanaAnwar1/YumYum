# YUMYUM - Food Recipes Application

YUMYUM is a Kotlin-based food recipes application that provides users with a wide variety of recipes from different cuisines and categories. This application utilizes Room database for local data storage, SharedPreferences for login management, and RetrofitClient for fetching data from TheMealDB.

## Features
- **User Authentication:** Secure login and signup functionality with data stored in SharedPreferences.
- **Cuisines & Categories:** Explore recipes based on specific cuisines (e.g., Egyptian, Chinese) and categories (e.g., beef, chicken).
- **Meal Details:** Each meal includes a details page with:
  - Meal name
  - Option to add to favorites
  - Pager containing:
    - Ingredients details
    - Cooking instructions with a video tutorial
- **Favorites Management:** View and manage favorite meals. Remove meals from favorites as needed.
- **Search Functionality:** Quickly find meals through a search feature.
- **Responsive Design:** Light and dark themes available, optimized for both portrait and landscape orientations.
- **Offline Support:** Handles internet disconnection and ensures app stability.

## Architecture
The application is structured using the MVVM architecture pattern, promoting a clean separation of concerns. The project is organized into the following folders:
- **ui:** Contains all UI components, including activities and fragments.
- **data:** Manages data sources, including the Room database and Retrofit services.
- **repository:** Handles data operations and provides a clean API for data access to the ViewModel.
- **util:** Contains utility classes and constants used throughout the app.

## Application Structure
### Activities
1. **Auth Activity:** Controls the signup, login, and splash screens.
2. **Meals Activity:** Displays fragments for:
   - Home
   - Favorites
   - Search
   - About

### Fragments
- **Home Fragment:** Displays recipes categorized by area and category.
- **Favorites Fragment:** Shows a list of favorite meals.
- **Search Fragment:** Allows users to search for specific meals.
- **Meal Views Fragment:** Displays a list of meals based on the selected category or area.
- **Meal Details Fragment:** Displays detailed information about selected meals.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/RanaAnwar1/YumYum.git
2. Open the project in Android Studio.
3. Build and run the application on an emulator or physical device.

## Video Demo
https://github.com/user-attachments/assets/b8d7d5a9-8bef-41e4-848c-b15dcf328409

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- [TheMealDB](https://www.themealdb.com/) for the recipe data.
- Android Developers for the documentation and resources.
