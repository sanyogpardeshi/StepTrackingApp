package com.example.ifit.IFitApp.Data.DataSource

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.History
import com.example.ifit.IFitApp.Domain.Model.StatusStack

@Database(
    entities = [Goal::class,History::class,StatusStack::class],
    version = 10
)
@TypeConverters(Converters::class)
abstract class IFitDatabase : RoomDatabase() {
    abstract val goalDao : GoalDao
    abstract val historyDao: HistoryDao
    abstract val statusStackDao : StatusStackDao
}