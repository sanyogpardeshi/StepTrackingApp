package com.example.ifit.IFitApp.Presentation.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/* CLASS THAT INCLUDING SCREEN OBJECTS IN IT. LATER ON WE WILL USE THESE OBJECTS TO CREATE BAR */
sealed class Screens(
    val title: String,
    val route: String,
    val icon:ImageVector
){
    object Home: Screens(
        title = "Home",
        route = "home_url",
        icon = Icons.Default.Home
    )
    object Goals: Screens(
        title = "Goals",
        route = "goals_url",
        icon = Icons.Default.ThumbUp
    )
    object History: Screens(
        title = "History",
        route = "history_url",
        icon = Icons.Default.DateRange
    )
    object SplashScreen :Screens(
        title = "Splash",
        route = "splash_url",
        icon = Icons.Default.Star
    )
    object SettingScreen :Screens(
        title = "Settings",
        route = "settings_url",
        icon = Icons.Filled.Menu
    )

}
