package com.example.ifit.IFitApp.Presentation.Components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Presentation.Navigation.Screens
import com.example.ifit.IFitApp.Presentation.States.GoalEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.GoalViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LongPressMenu(isLongPressed:Boolean, goal: Goal, navController: NavController, viewModel: GoalViewModel = hiltViewModel()){

    val isEditable = viewModel.goalStates.value.isGoalEditable

    val enterTransition = remember {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(450)
            ) + fadeIn(
                initialAlpha = 0.3f,
                animationSpec = tween(450)
            )
        }
        val exitTransition = remember {
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(450)
            ) + fadeOut(
                animationSpec = tween(450)
            )
        }
        AnimatedVisibility(
            visible = isLongPressed,
            enter = enterTransition,
            exit = exitTransition
        ){
            Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = { viewModel.onInteract(GoalEvents.activateGoal(goal)) }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Activate")
                    }
                    IconButton(onClick = { viewModel.onInteract(GoalEvents.editGoal(goal)) }, enabled = isEditable) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = {
                        viewModel.onInteract(GoalEvents.deleteGoal(goal))
                        }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
            }
        }
}