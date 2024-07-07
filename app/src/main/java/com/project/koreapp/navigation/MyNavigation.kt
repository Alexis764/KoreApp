package com.project.koreapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.koreapp.feature.add_list.MyAddListScreen
import com.project.koreapp.feature.main.MyMainScreen
import com.project.koreapp.feature.splash.MySplashScreen

@Composable
fun MyNavigation(navController: NavHostController, isDialogChanged: (Boolean) -> Unit) {
    NavHost(navController = navController, startDestination = Routes.SplashScreen.route) {
        composable(Routes.SplashScreen.route) {
            MySplashScreen(navController)
        }

        composable(Routes.MainScreen.route) {
            MyMainScreen(navController) { isDialogChanged(it) }
        }

        composable(
            Routes.AddListScreen.route,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("day") { type = NavType.IntType }
            )
        ) {
            val year = it.arguments?.getInt("year") ?: 0
            val month = it.arguments?.getInt("month") ?: 0
            val day = it.arguments?.getInt("day") ?: 0

            MyAddListScreen(
                year,
                month,
                day,
                navController
            ) { isDialog -> isDialogChanged(isDialog) }
        }
    }
}