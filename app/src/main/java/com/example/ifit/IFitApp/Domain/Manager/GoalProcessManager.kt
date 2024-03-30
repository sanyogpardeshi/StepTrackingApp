package com.example.ifit.IFitApp.Domain.Manager

/* MANAGER CLASS THAT COLLECTS ALL MANAGER MEMBERS */

data class GoalProcessManager(
    val listGoal : GetGoalsProcess,
    val addGoal : AddGoalProcess,
    val deleteGoal: DeleteGoalProcess,
    val getActiveGoal: GetActiveGoalProcess,
    val getGoalById : GetGoalByIdProcess,
    val getGoalByName : GetGoalByNameProcess
)
