package com.project.koreapp.navigation

sealed class Routes(val route: String) {
    data object SplashScreen : Routes("splashScreen")

    data object MainScreen : Routes("mainScreen")

    data object AddListScreen : Routes("addListScreen/{year}/{month}/{day}") {
        fun createRoute(year: Int, month: Int, day: Int) = "addListScreen/$year/$month/$day"
    }
}