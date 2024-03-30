package com.example.ifit.IFitApp.Presentation.States

import com.example.ifit.IFitApp.Domain.Model.Goal

data class HomeStates(
    val isHistoryModeOn: Boolean = true,
    val activeGoal : Goal? = null,
    val stepEntered : String = "",
    val goalStepCount : String = "",
    val done : String = "",
    val left : String = "",
    val goalName : String = "",
    val totalSteps : String = "",
    val percentage : Float = 0f,
    val openAddStepPopUp : Boolean = false,
)
