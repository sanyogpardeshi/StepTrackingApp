package com.example.ifit.IFitApp.Data.Repository

import com.example.ifit.IFitApp.Domain.Model.History
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.sql.Timestamp

interface HistoryRepository {

    fun getHistoryRecords(): Flow<List<History>>

    suspend fun getHistoryById(id:Int): History?

    fun getHistoryRecordForToday(day:Int,month:Int,year:Int): History?

    fun getLastSevenDays(): Flow<List<History>>

    suspend fun addHistory(history: History)

    suspend fun deleteHistory()

}