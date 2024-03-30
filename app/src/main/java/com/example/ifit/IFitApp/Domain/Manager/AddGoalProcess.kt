package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.GoalRepository
import com.example.ifit.IFitApp.Domain.Error.ErrorTypes
import com.example.ifit.IFitApp.Domain.Model.Goal

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* ADD GOAL TO DB. IF IT IS EDITING, IT DOESN'T CHECK NAME. */

class AddGoalProcess(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(goal: Goal,isEditing:Boolean)  {
        val _goal: Goal? = goalRepository.getGoalByName(goal.title)
        if(_goal!=null && !isEditing){
            throw ErrorTypes.ExistingGoalNameException()
        }else{
            goalRepository.addGoal(goal)
        }
    }
}
