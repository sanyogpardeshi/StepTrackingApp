package com.example.ifit.IFitApp.Data.DataSource

import androidx.room.*
import com.example.ifit.IFitApp.Domain.Model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Query("SELECT * FROM goal")
    fun getGoals(): Flow<List<Goal>>

    @Query("SELECT * FROM goal WHERE id=:id")
    suspend fun getGoalById(id:Int):Goal?

    @Query("SELECT * FROM goal WHERE title=:title")
    suspend fun getGoalByName(title:String):Goal?

    @Query("SELECT * FROM goal WHERE isActive=:isActive")
    suspend fun getActiveGoal(isActive: Boolean):Goal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoal(goal:Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

}