package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Model.Goal

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH GOAL BY ID */

class GetGoalByIdProcess(
    private val repository: GoalRepository
) {

    suspend operator fun invoke(id:Int) : Goal {
        return repository.getGoalById(id)!!
    }
}