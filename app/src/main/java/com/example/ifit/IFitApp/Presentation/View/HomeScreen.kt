package com.example.ifit.IFitApp.Presentation.View

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ifit.IFitApp.Presentation.Components.AddStepPopUp
import com.example.ifit.IFitApp.Presentation.Components.CircularProgressbar
import com.example.ifit.IFitApp.Presentation.Navigation.Screens
import com.example.ifit.IFitApp.Presentation.States.HomeEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.HistoryViewModel
import com.example.ifit.IFitApp.Presentation.ViewModel.HomeViewModel
import com.example.ifit.R
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateHomeScreen(viewModel: HomeViewModel = hiltViewModel()){

    val homeStates = viewModel.homeStates.value
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect{ event ->
            when(event) {
                is HomeViewModel.UiEvent.UnknownError -> {
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
    ){scaffoldPadding->
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .padding(top = 10.dp)){
            Spacer(modifier = Modifier.height(1.dp).padding(scaffoldPadding))
            Card(backgroundColor = Color.White,
                elevation = 8.dp,
                shape = RoundedCornerShape(6.dp),
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(id = R.drawable.target_icon), contentDescription = "Active Icon", modifier = Modifier.fillMaxWidth(0.3f))
                    Column(modifier = Modifier.fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "Active Goal", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
                        Text(text = homeStates.goalName, style = MaterialTheme.typography.subtitle1,textAlign = TextAlign.Center)
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            //For showing percentage bar
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                CircularProgressbar(percentage =homeStates.percentage , number = 100)
            }
            Spacer(modifier = Modifier.height(60.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                //For showing info about goal
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.target_icon), contentDescription = "Target Icon")
                    Text(text = "Target", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
                    Text(text = homeStates.goalStepCount, style = MaterialTheme.typography.subtitle1,textAlign = TextAlign.Center)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id =R.drawable.done_icon) , contentDescription = "Done Icon")
                    Text(text = "Done", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
                    Text(text = homeStates.done, style = MaterialTheme.typography.subtitle1,textAlign = TextAlign.Center)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id =R.drawable.left_icon) , contentDescription = "Left Icon")
                    Text(text = "Left", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
                    Text(text = homeStates.left, style = MaterialTheme.typography.subtitle1,textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                FloatingActionButton(onClick = {
                    if(homeStates.activeGoal!=null){
                        viewModel.onInteract(HomeEvents.openAddStepPopup)
                    }else{
                        localCoroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please activate a goal first! Press long on a goal and click activate icon."
                            )
                        }
                    }
                },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Steps")
                }
            }
        }
        if(homeStates.openAddStepPopUp){
            AddStepPopUp()
        }
    }
}