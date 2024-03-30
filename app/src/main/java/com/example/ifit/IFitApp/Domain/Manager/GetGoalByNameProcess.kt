package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Model.Goal

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH GOAL BY NAME */

class GetGoalByNameProcess(private val goalRepository: GoalRepository) {

    suspend operator fun invoke(name:String) : Goal? {
        return goalRepository.getGoalByName(name)
    }
}