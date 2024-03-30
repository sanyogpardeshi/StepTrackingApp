package com.example.ifit.IFitApp.Presentation.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ifit.IFitApp.Presentation.States.SettingEvents
import com.example.ifit.IFitApp.Presentation.ViewModel.SettingViewModel

@Composable
fun CreateSettingScreen(viewModel: SettingViewModel = hiltViewModel()){

    val goalEditable = viewModel.settingStates.value.isGoalEditable
    val historyMode = viewModel.settingStates.value.isHistoryRecordable


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Card(
              modifier = Modifier
                  .fillMaxWidth(.8f)
                  .fillMaxHeight(.7f)
                  .padding(bottom = 50.dp)
            , shape = RoundedCornerShape(8.dp)
            , backgroundColor = MaterialTheme.colors.background
            , elevation = 8.dp
            , border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                , verticalArrangement = Arrangement.Center
                , horizontalAlignment = Alignment.CenterHorizontally) {

                Image(painter = painterResource(id = com.example.ifit.R.drawable.settings_image), contentDescription ="Setting Image"
                    , contentScale = ContentScale.Fit
                    , modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxWidth(.7f)
                        .fillMaxHeight(.3f)
                        .padding(top = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Preferences"
                    , fontWeight = FontWeight.SemiBold
                    , textAlign = TextAlign.Center
                    , maxLines = 1
                    , style = MaterialTheme.typography.h4
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier
                    .fillMaxWidth(.7f)
                    .fillMaxHeight()
                    .padding(start = 5.dp)){
                    Column(modifier=Modifier.fillMaxSize()){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                            , shape = RoundedCornerShape(8.dp)
                            , backgroundColor = MaterialTheme.colors.background
                            , elevation = 8.dp
                            , border = BorderStroke(1.dp, Color.LightGray)
                        ){
                            Column(  modifier = Modifier
                                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Goal Edit Mode",modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 2.dp), textAlign = TextAlign.Center)
                                Switch(checked = goalEditable, onCheckedChange = {viewModel.onInteract(SettingEvents.changeGoalEditableMode(goalEditable)) } )
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                            , shape = RoundedCornerShape(8.dp)
                            , backgroundColor = MaterialTheme.colors.background
                            , elevation = 8.dp
                            , border = BorderStroke(1.dp, Color.LightGray)
                        ){
                            Column(  modifier = Modifier
                                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "History Record Mode", modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 2.dp), textAlign = TextAlign.Center)
                                Switch(checked = historyMode, onCheckedChange = {viewModel.onInteract(SettingEvents.changeHistoryRecordMode(historyMode)) })
                            }

                        }
                    }
                }
            }
        }
    }
}