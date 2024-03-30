package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Data.DataSource.HistoryDao
import com.example.ifit.IFitApp.Domain.Model.History
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.sql.Timestamp

class HistoryRepositoryImpl(private val historyDao: HistoryDao) : HistoryRepository{

    override fun getHistoryRecords(): Flow<List<History>> {
        return historyDao.getHistoryRecords()
    }

    override suspend fun getHistoryById(id: Int): History? {
        return historyDao.getHistoryById(id)
    }

    override fun getHistoryRecordForToday(day: Int, month: Int, year: Int):History?{
        return historyDao.getHistoryRecordForToday(day,month,year)
    }

    override fun getLastSevenDays(): Flow<List<History>> {
        return historyDao.getLastSevenDays()
    }

    override suspend fun addHistory(history: History) {
        historyDao.addHistory(history = history)
    }

    override suspend fun deleteHistory() {
        historyDao.deleteHistory()
    }
}