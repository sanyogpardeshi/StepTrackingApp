package com.example.ifit.IFitApp.Presentation.States

import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.History

data class HistoryStates(
    val goals: List<History> = emptyList(),
    val actualGoals: List<Goal> = emptyList(),
    val activeGoal : String = "",
    val goal: History? = null,
    val stepCount : Int? =null,
    val date: String = "",
    val goalEditToggled: Boolean = false,
    val isGoalEditable : Boolean = false,
    val editHistoryRecordPopup : Boolean = false,
    val goalOrder : GoalListOrder = GoalListOrder.listAllTime(),
    val stepEntered : String = "",
    val chosenGoal : String = ""

)
