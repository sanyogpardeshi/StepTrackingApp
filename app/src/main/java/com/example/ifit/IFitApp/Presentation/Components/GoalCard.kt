package com.example.ifit.IFitApp.Presentation.Components


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ifit.IFitApp.Domain.Model.Goal
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun GoalCard(
    modifier: Modifier = Modifier,
    goal: Goal,
    isToggled: Boolean,
    isLongPressed:Boolean,
    onShowDetail:() -> Unit,
    navController: NavController
)
{
    var backgroundColorOfCard : Color = Color.White
    if(goal.isActive){
        backgroundColorOfCard = Color.Green
    }
    val transitionState = remember {MutableTransitionState(isToggled).apply { targetState = !isToggled }}
    val transition = updateTransition(transitionState,"transition")
    val arrowDegree by transition.animateFloat({ tween(durationMillis = 450) }, label = "arrowDegree") {
        if (isToggled) 180f else 360f
    }
    Card(backgroundColor = backgroundColorOfCard,
        elevation = 8.dp,
        shape = RoundedCornerShape(6.dp),
        modifier= Modifier
            .fillMaxWidth()
            .padding(6.dp)) {
        Column() {
            Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
                DetailArrow(onShowDetail,arrowDegree)
                GoalTitle(goal)
            }
            GoalContent(visible = isToggled,goal=goal)
            LongPressMenu(isLongPressed,goal,navController)
        }
    }
}


@Composable
fun GoalTitle(goal: Goal){
    Text(text = goal.title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1)
}

@Composable
fun DetailArrow(onClick: () -> Unit,degree:Float){
    IconButton(onClick = onClick, modifier = Modifier.rotate(degree)) {
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Show Details")
    }
}

@Composable
fun GoalContent(visible:Boolean = true,goal: Goal){

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
        visible = visible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val cal = Calendar.getInstance()
            cal.setTime(goal.timestamp)
            Row(){
                Icon(imageVector = Icons.Default.Star, contentDescription = "Step Icon")
                Text(
                    text = goal.stepCount.toString() + " steps",
                    textAlign = TextAlign.Center
                )
            }
            Row() {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Icon")
                Text(
                    text = cal.get(Calendar.DAY_OF_MONTH).toString() + "/" + cal.get(Calendar.MONTH).plus(1).toString() + "/" + cal.get(Calendar.YEAR).toString(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}