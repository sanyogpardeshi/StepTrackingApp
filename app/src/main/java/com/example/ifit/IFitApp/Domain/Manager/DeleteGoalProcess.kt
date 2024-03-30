package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Model.Goal

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* DELETE SPECIFIED GOAL */

class DeleteGoalProcess(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(goal:Goal){
        repository.deleteGoal(goal)
    }
}