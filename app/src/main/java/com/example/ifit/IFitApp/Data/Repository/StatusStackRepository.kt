package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Domain.Model.StatusStack

interface StatusStackRepository {

    suspend fun getStatusStackElementById(goalId:Int): StatusStack?

    suspend fun addStatusElementToStack(statusStack: StatusStack)

    suspend fun deleteElementFromStack(statusStack: StatusStack)
}