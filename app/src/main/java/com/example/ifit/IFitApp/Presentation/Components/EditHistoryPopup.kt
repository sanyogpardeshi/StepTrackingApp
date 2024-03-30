package com.example.ifit.IFitApp.Presentation.Components

import com.example.ifit.IFitApp.Presentation.ViewModel.HistoryViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Presentation.States.HistoryEvents

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EdithistoryPopUp(viewModel: HistoryViewModel= hiltViewModel()){
    val stepCount = viewModel.historyStates.value.stepEntered
    val chosenGoal = viewModel.historyStates.value.chosenGoal
    val data : List<Goal> = viewModel.historyStates.value.actualGoals
    val isEnabled = stepCount!=""

    Box(modifier= Modifier
        .fillMaxSize(), contentAlignment = Alignment.Center){

        Card(modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.5f),
            border = BorderStroke(0.5.dp,Color.LightGray),
            elevation = 6.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Edit History", style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 10.dp))
                Button(onClick = { viewModel.onInteract(HistoryEvents.closePopUp) },
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
                dropDownGoalSelection(data,chosenGoal)
                OutlinedTextField(value = stepCount, onValueChange = {viewModel.onInteract(HistoryEvents.stepsEntered(it))}, label = {Text("New Step Count")}, singleLine = true)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {viewModel.onInteract(HistoryEvents.addHistoryNew)},
                    shape = RoundedCornerShape(6.dp),
                    enabled =isEnabled,
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = MaterialTheme.colors.onSecondary, backgroundColor = MaterialTheme.colors.primary, contentColor = Color.White, disabledContentColor = Color.White),
                )  {
                    Text(text = "Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dropDownGoalSelection(data:List<Goal>,activeGoal: String,viewModel: HistoryViewModel= hiltViewModel()) {

    var chosenGoal by remember {
        mutableStateOf(activeGoal)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        TextField(
            value = chosenGoal,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Goal") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            data.forEach { selectedOption ->
                DropdownMenuItem(onClick = {
                    chosenGoal = selectedOption.title
                    expanded = false
                    viewModel.onInteract(HistoryEvents.chosenGoal(chosenGoal))
                }) {
                    Text(text = selectedOption.title)
                }
            }
        }
    }
}