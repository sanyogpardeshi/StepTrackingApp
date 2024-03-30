package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Model.Goal
import kotlinx.coroutines.flow.Flow

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH GOALS */

class GetGoalsProcess(
    private val goalRepository: GoalRepository
) {

    operator fun invoke() : Flow<List<Goal>>{
        return goalRepository.getGoals()
    }
}