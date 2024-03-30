package com.example.ifit.IFitApp.Presentation.States

import com.example.ifit.IFitApp.Domain.Model.Goal

sealed class GoalEvents {
    data class  titleEntered(val value:String) : GoalEvents()
    data class  countEntered(val value:String) : GoalEvents()
    data class editGoal(val goal: Goal) : GoalEvents()
    data class deleteGoal(val goal: Goal) : GoalEvents()
    data class activateGoal(val goal:Goal) : GoalEvents()
    data class ShowDetails(val id:Int?) : GoalEvents()
    data class LongPressMenu(val id:Int?) : GoalEvents()

    object getGoals : GoalEvents()
    object openAddGoalPopup : GoalEvents()
    object closeAddGoalPopup : GoalEvents()
    object addGoal : GoalEvents()
    object restoreGoal : GoalEvents()
}