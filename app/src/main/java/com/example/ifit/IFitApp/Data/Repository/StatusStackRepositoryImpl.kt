package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Data.DataSource.StatusStackDao
import com.example.ifit.IFitApp.Domain.Model.StatusStack

class StatusStackRepositoryImpl(private val statusStackDao: StatusStackDao) : StatusStackRepository {

    override suspend fun getStatusStackElementById(goalId: Int): StatusStack? {
        return statusStackDao.getStatusStackElementById(goalId)
    }

    override suspend fun addStatusElementToStack(statusStack: StatusStack) {
       statusStackDao.addStatusElementToStack(statusStack = statusStack)
    }

    override suspend fun deleteElementFromStack(statusStack: StatusStack) {
        statusStackDao.deleteElementFromStack(statusStack = statusStack)
    }
}