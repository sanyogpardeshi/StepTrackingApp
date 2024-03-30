package com.example.ifit.IFitApp.Data.DataSource

import androidx.room.*
import com.example.ifit.IFitApp.Domain.Model.History
import com.example.ifit.IFitApp.Domain.Model.StatusStack
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

@Dao
interface StatusStackDao {

    @Query("SELECT * FROM statusstack WHERE goalId=:goalId")
    suspend fun getStatusStackElementById(goalId:Int): StatusStack?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStatusElementToStack(statusStack: StatusStack)

    @Delete
    suspend fun deleteElementFromStack(statusStack: StatusStack)
}