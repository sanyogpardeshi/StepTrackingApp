package com.example.ifit.IFitApp.Presentation.View

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ifit.IFitApp.Presentation.Components.AddGoalPopup
import com.example.ifit.IFitApp.Presentation.Components.GoalCard
import com.example.ifit.IFitApp.Presentation.Navigation.Screens
import com.example.ifit.IFitApp.Presentation.States.GoalEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.GoalViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateGoalScreen(
    navController:NavController,
    viewModel: GoalViewModel= hiltViewModel()
){
    val state = viewModel.goalStates.value
    val expandedCardIds by viewModel.toggledCards.collectAsState()
    val longPressedCardIds by viewModel.longPressedCards.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect{ event ->
            when(event) {
                is GoalViewModel.UiEvent.GoalAction -> {
                    navController.navigate(Screens.Goals.route)
                }
                is GoalViewModel.UiEvent.ExistingGoalAction -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "There is already a goal exist with same name."
                        )
                    }
                }
                is GoalViewModel.UiEvent.UnknownError ->{
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Something went wrong. Please try again!"
                        )
                    }
                }
                else -> {
                    localCoroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Goal deleted!",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed){
                            viewModel.onInteract(GoalEvents.restoreGoal)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.padding(bottom = 50.dp),
        backgroundColor = Color.Transparent
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(12.dp),
                    shape = RoundedCornerShape(6.dp),
                    backgroundColor = MaterialTheme.colors.background,
                    border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
                    elevation = 6.dp
                ) {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()){
                        if(state.goals.isNotEmpty()){
                            items(state.goals){goal->
                                GoalCard(goal = goal,
                                    isToggled =expandedCardIds.contains(goal.id) ,
                                    isLongPressed = longPressedCardIds.contains(goal.id),
                                    onShowDetail = { viewModel.onInteract(GoalEvents.ShowDetails(goal.id)) },
                                    modifier = Modifier.pointerInput(Unit){
                                        if(!goal.isActive){
                                            detectTapGestures(
                                                onLongPress = { viewModel.onInteract(GoalEvents.LongPressMenu(goal.id)) }
                                            )
                                        }
                                    }, navController = navController)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                FloatingActionButton(onClick = { viewModel.onInteract(GoalEvents.openAddGoalPopup) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal")
                }
            }
            if(state.addGoalPopUpActive){
                AddGoalPopup(navController)
            }
        }
    }
}




