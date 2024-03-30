package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Model.Goal

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH ACTIVE GOAL */

class GetActiveGoalProcess(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(boolean: Boolean) : Goal?{
        val goal:Goal? =  repository.getActiveGoal(boolean)
        return goal
    }
}