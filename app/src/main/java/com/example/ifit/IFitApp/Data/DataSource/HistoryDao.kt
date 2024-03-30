package com.example.ifit.IFitApp.Data.DataSource

import androidx.room.*
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.History
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.sql.Timestamp

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getHistoryRecords(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE id=:id")
    suspend fun getHistoryById(id:Int): History?

    @Query("SELECT * FROM history WHERE day=:day AND month=:month AND year=:year")
    fun getHistoryRecordForToday(day:Int,month:Int,year:Int): History?

    @Query("SELECT * from history WHERE CAST((date / 1000) AS INTEGER) BETWEEN strftime('%s','now','-7 days') AND strftime('%s','now')  ORDER BY id DESC;")
    fun getLastSevenDays(): Flow<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistory(history: History)

    @Query("DELETE  FROM history")
    suspend fun deleteHistory()
}