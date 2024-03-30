package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Domain.Model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getGoals() :Flow<List<Goal>>

    suspend fun getGoalById(id:Int) : Goal?

    suspend fun getGoalByName(title:String):Goal?

    suspend fun getActiveGoal(boolean: Boolean) : Goal?

    suspend fun addGoal(goal: Goal)

    suspend fun deleteGoal(goal: Goal)
}