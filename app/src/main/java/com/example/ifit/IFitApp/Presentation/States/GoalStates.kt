package com.example.ifit.IFitApp.Presentation.States

import com.example.ifit.IFitApp.Domain.Model.Goal

data class GoalStates(
    val goals: List<Goal> = emptyList(),
    val goalsToggled: Boolean = false,
    val addGoalPopUpActive : Boolean = false,
    val isGoalEditable : Boolean  = true,

    val title : String = "",
    val step : String = "",

)

