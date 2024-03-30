package com.example.ifit.IFitApp.Presentation.Navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ifit.IFitApp.Presentation.States.HistoryEvents
import com.example.ifit.IFitApp.Presentation.States.HomeEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.HistoryViewModel
import com.example.ifit.IFitApp.Presentation.ViewModel.HomeViewModel
import com.example.ifit.R

/* NAVIGATION COMPOSE */
/* IN THIS KOTLIN FILE WE SIMPLY CREATE NAVIGATION BAR. */
/* APP USES SCAFFOLD FOR DECLARE TOP AND BOTTOM NAVIGATION BARS. WE GIVE THE SCREENS ITSELFS AS PARAMETER TO SCAFFOLD BODY. */
/* SO WHEN USER NAVIGATE BETWEEN SCREENS THE ONLY THING THAT CHANGES IS ACTUALLY SCAFFOLD'S BODY WHICH CONTAIN RELATED SCREEN */
/* WE PASS NAVCONTROLLER AS PARAMETER TO NAVGRAPH SO WE CAN PASS SCREEN ROUTE. THEN NAVGRAPH CATCHES IT AND BRINGS US RELATED SCREEN */

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateNavigationBar(viewModel: HistoryViewModel = hiltViewModel()){
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.app_bg),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Scaffold(modifier = Modifier.fillMaxSize(), backgroundColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                if (currentRoute(navController) != Screens.SplashScreen.route) {
                    Column() {
                        TopAppBar(navController)
                    }
                }
            },
            bottomBar = {
                if (currentRoute(navController) != Screens.SplashScreen.route) {
                    BottomBar(navController = navController)
                }
            }
        ) {
            NavGraph(navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopAppBar(navController : NavHostController, viewModel: HistoryViewModel = hiltViewModel()){
    TopAppBar(
        title = {
            Text(text = "IFit")
        },
        actions = {
            Column(){
                IconButton(onClick = {navController.navigate(Screens.SettingScreen.route)}) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        Screens.Home,
        Screens.Goals,
        Screens.History
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation() {
        screens.forEach{screen->
            AddItems(screen=screen,currentDestination=currentDestination,navController=navController)
        }
    }
}

@Composable
fun RowScope.AddItems(
    screen:Screens,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    if (currentDestination != null) {
        BottomNavigationItem(
            label = {
                Text(text = screen.title)
            },
            icon = {
                Icon(imageVector = screen.icon, contentDescription = "Navigation Icon")
            },
            selected = currentDestination.hierarchy.any{
                it.route==screen.route
            },
            onClick = {
                navController.navigate(screen.route)
            }
        )
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}