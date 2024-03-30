package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Data.DataSource.GoalDao
import com.example.ifit.IFitApp.Domain.Model.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepositoryImpl(private val dao: GoalDao) : GoalRepository {
    override fun getGoals(): Flow<List<Goal>> {
        return dao.getGoals()
    }

    override suspend fun getGoalById(id: Int): Goal? {
        return dao.getGoalById(id)
    }

    override suspend fun getGoalByName(title: String): Goal? {
        return dao.getGoalByName(title)
    }

    override suspend fun getActiveGoal(boolean: Boolean): Goal? {
        return dao.getActiveGoal(boolean)
    }

    override suspend fun addGoal(goal: Goal) {
        return dao.addGoal(goal)
    }

    override suspend fun deleteGoal(goal: Goal) {
        return dao.deleteGoal(goal)
    }
}