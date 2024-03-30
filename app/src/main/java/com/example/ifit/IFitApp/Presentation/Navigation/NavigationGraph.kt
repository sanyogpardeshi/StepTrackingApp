package com.example.ifit.IFitApp.Presentation.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ifit.IFitApp.Presentation.View.*

/* NAVIGATION GRAPH WHICH CONTROLS THE REDIRECT OPERATIONS WHEN USER CLICK ON A SCREEN ICON IN NAVIGATION BAR */
/* SIMPLY CATCH THE OPERATION AND GET ROUTE THEN COMPOSE THE RELATED SCREEN. */
/* BY DEFAULT SPLASH SCREEN IS DEFINED AS START SCREEN BUT AUTOMATICALLY IT REDIRECT APPLICATION TO STATUS SCREEN */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route){
        composable(route=Screens.Home.route){
            CreateHomeScreen()
        }
        composable(route=Screens.Goals.route){
            CreateGoalScreen(navController)
        }
        composable(route=Screens.History.route){
            CreateHistoryScreen(navController)
        }
        composable(route=Screens.SplashScreen.route){
            CreateSplashScreen(navController)
        }
        composable(route=Screens.SettingScreen.route){
            CreateSettingScreen()
        }
    }
}