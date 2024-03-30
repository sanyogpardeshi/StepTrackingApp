package com.example.ifit.IFitApp.Presentation.Components

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ifit.IFitApp.Presentation.Navigation.Screens
import com.example.ifit.IFitApp.Presentation.States.GoalEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.GoalViewModel
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddGoalPopup(navController: NavController,viewModel: GoalViewModel= hiltViewModel()){
    val state = viewModel.goalStates.value
    val isEnabled = (state.title!="") && state.step!=""
    val scaffoldState = rememberScaffoldState()


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is GoalViewModel.UiEvent.GoalAction -> {
                    navController.navigate(Screens.Goals.route)
                }
                is GoalViewModel.UiEvent.ExistingGoalAction -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "There is already a goal exist with same name."
                    )
                }
                else -> {}
            }
        }
    }

    Box(modifier= Modifier
        .fillMaxSize(), contentAlignment = Alignment.Center){

        Card(modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.4f),
            border = BorderStroke(0.5.dp,Color.LightGray),
            elevation = 6.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "My Goal", style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 10.dp))
                Button(onClick = { viewModel.onInteract(GoalEvents.closeAddGoalPopup) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White),
                    modifier = Modifier
                        .size(35.dp)) {
                    Text(text = "X", textAlign = TextAlign.Center,color = Color.White)
                }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(6.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = state.title, onValueChange = {viewModel.onInteract(GoalEvents.titleEntered(it))}, label = {Text("Title")} , singleLine = true )
                OutlinedTextField(value = state.step, onValueChange = {viewModel.onInteract(GoalEvents.countEntered(it))}, label = {Text("Step Count")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) , singleLine = true)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.onInteract(GoalEvents.addGoal)},
                    shape = RoundedCornerShape(6.dp),
                    enabled = isEnabled,
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = MaterialTheme.colors.onSecondary, backgroundColor = MaterialTheme.colors.primary, contentColor = Color.White, disabledContentColor = Color.White),
                )  {
                    Text(text = "Save")
                }
            }
        }
    }
}