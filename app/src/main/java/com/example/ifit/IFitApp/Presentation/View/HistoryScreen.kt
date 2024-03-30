package com.example.ifit.IFitApp.Presentation.View

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Presentation.Components.EdithistoryPopUp
import com.example.ifit.IFitApp.Presentation.Components.GoalCard
import com.example.ifit.IFitApp.Presentation.Components.PieChart
import com.example.ifit.IFitApp.Presentation.Navigation.Screens
import com.example.ifit.IFitApp.Presentation.States.GoalEvents
import com.example.ifit.IFitApp.Presentation.States.GoalListOrder
import com.example.ifit.IFitApp.Presentation.States.HistoryEvents
import com.example.ifit.IFitApp.Presentation.States.HomeEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.GoalViewModel
import com.example.ifit.IFitApp.Presentation.ViewModel.HistoryViewModel
import com.example.ifit.MainActivity
import com.example.ifit.R
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateHistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()

){
    /*val popUpState = viewModel.historyStates.value.editHistoryRecordPopup
    val orderType : GoalListOrder = viewModel.historyStates.value.goalOrder
    val records = viewModel.historyStates.value.goals
    val todayRecord = viewModel.historyStates.value.goal
    val isEditable = viewModel.historyStates.value.isGoalEditable*/
    val states = viewModel.historyStates.value
    val date = viewModel.historyStates.value.date

    val context = LocalContext.current
    var counter = 0
    val openDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()


    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.onInteract(HistoryEvents.getRecordForDate(day = dayOfMonth, month = month, year = year))
            //date = "$dayOfMonth/$month/$year"
        }, year, month, day
    )
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect{ event ->
            when(event) {
                is HistoryViewModel.UiEvent.ReloadAction -> {
                    navController.navigate(Screens.History.route)
                }
                is HistoryViewModel.UiEvent.UnknownError -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Something went wrong. Please try again!"
                        )
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

        Column(modifier = Modifier.fillMaxSize().padding(scaffoldPadding), horizontalAlignment = Alignment.CenterHorizontally){
            if(openDialog.value){
                ClearHistoryDialog(openDialog)
            }
            Row(modifier = Modifier
                .fillMaxHeight(0.15f)
                .fillMaxWidth()
                .padding(top = 5.dp, start = 5.dp, end = 5.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                OrderSection(goalListOrder = states.goalOrder, onOrderChange = {viewModel.onInteract(HistoryEvents.changeOrderType(it))})
                DefaultRadioButton(text = if(date=="") "Select Date" else date,
                    selected = states.goalOrder is GoalListOrder.getRecordForDay,
                    onSelect = {datePickerDialog.show()})
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(onClick = {openDialog.value=!openDialog.value},
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete History")
                }
            }
            Card(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(10.dp),
                shape = RoundedCornerShape(6.dp),
                backgroundColor = MaterialTheme.colors.background,
                border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
                elevation = 6.dp
            ) {
                if(date==""){
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()){
                        if(states.goals.isNotEmpty()){
                            items(states.goals){goal->
                                Card(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    backgroundColor = MaterialTheme.colors.background,
                                    border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
                                    elevation = 6.dp){
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.35f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                            Icon(painter = painterResource(id = R.drawable.baseline_bookmark_24), contentDescription = "")
                                            Text(text = goal.goalName, style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center,color = Color.Black)
                                        }
                                        Column(modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.3f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                            Icon(painter = painterResource(id = R.drawable.step_icon), contentDescription = "" )
                                            Text(text = goal.totalSteps.toString(), style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center)
                                        }
                                        Column(modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.6f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                            Icon(painter = painterResource(id = R.drawable.calender_icon), contentDescription = "" )
                                            Text(text = goal.day.toString() + "/" + goal.month.toString() + "/" + goal.year.toString(), style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center)
                                        }
                                        IconButton(onClick = { viewModel.onInteract(HistoryEvents.editHistory(goal)) }, enabled = states.isGoalEditable) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                                        }
                                    }
                                }
                                counter+= goal.totalSteps
                            }
                        }
                    }
                }else{
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                        shape = RoundedCornerShape(6.dp),
                        backgroundColor = MaterialTheme.colors.background,
                        border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
                        elevation = 6.dp) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            if(states.goal!=null){
                                Column(modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.35f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                    Icon(painter = painterResource(id = R.drawable.baseline_bookmark_24), contentDescription = "")
                                    Text(text = states.goal.goalName, style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center,color = Color.Black)
                                }
                                Column(modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.3f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                    Icon(painter = painterResource(id = R.drawable.step_icon), contentDescription = "" )
                                    Text(text = states.goal.totalSteps.toString(), style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center)
                                }
                                Column(modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.6f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                    Icon(painter = painterResource(id = R.drawable.calender_icon), contentDescription = "" )
                                    Text(text = states.goal.day.toString() + "/" + states.goal.month.toString() + "/" + states.goal.year.toString(), style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center)
                                }
                                IconButton(onClick = { viewModel.onInteract(HistoryEvents.editHistory(states.goal)) }, enabled = states.isGoalEditable) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                                }
                            }else{
                                Text(text = "No record for selected date.")
                            }
                        }
                    }
                }

            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp, start = 80.dp), contentAlignment = Alignment.CenterStart){
                PieChart(data = states.goals,today = states.goal)

            }
        }
        if(states.editHistoryRecordPopup){
            EdithistoryPopUp()
        }

    }

}

@Composable
fun OrderSection(goalListOrder: GoalListOrder, onOrderChange :(GoalListOrder) -> Unit){
        DefaultRadioButton(text = "All Records",
            selected = goalListOrder is GoalListOrder.listAllTime,
            onSelect = {onOrderChange(GoalListOrder.listAllTime())})
        Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun DefaultRadioButton(
    text:String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(modifier=modifier,
        verticalAlignment = Alignment.CenterVertically) {

        RadioButton(selected = selected, onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary, unselectedColor = MaterialTheme.colors.onBackground))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.body1)
    }

}

@Composable
fun ClearHistoryDialog(openDialog: MutableState<Boolean>, viewModel: HistoryViewModel = hiltViewModel()) {
    AlertDialog(
        onDismissRequest = {openDialog.value=!openDialog.value },
        confirmButton = {
            TextButton(onClick = {
                openDialog.value=!openDialog.value
                viewModel.onInteract(HistoryEvents.clearHistory)
            })
            { Text(text = "Delete") }
        },
        dismissButton = {
            TextButton(onClick = {openDialog.value=!openDialog.value })
            { Text(text = "Cancel") }
        },
        title = { Text(text = "Please Confirm") },
        text = { Text(text = "Are you sure to delete all records?") }
    )
}


