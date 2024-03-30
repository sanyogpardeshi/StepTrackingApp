package com.example.ifit.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.ifit.IFitApp.Data.DataSource.IFitDatabase
import com.example.ifit.IFitApp.Data.Repository.*
import com.example.ifit.IFitApp.Domain.Manager.*
import com.example.ifit.IFitApp.Domain.PreferenceManager.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

/* EVERY APP SHOULD HAVE DEPENDENCY INJECTION MANAGEMENT */
/* INSTEAD OF CREATING OBJECTS AND CAUSE MULTIPLE INSTANCES WE CAN SIMPLY INITIALIZE OBJECTS IN THE BEGINNING OF THE APPLICATION. */
/* THIS CLASS INITIATES THE OBJECTS WITH SINGLETON, WHICH CREATE JUST ONE INSTANCE, AND PROVIDE US TO SIMPLY INJECT THEM OUR CONSTRUCTORS */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun initiateDatabase(app:Application) : IFitDatabase{
        return Room.databaseBuilder(
            app,
            IFitDatabase::class.java,
            "IFitDB"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun initiateRepository(db:IFitDatabase):GoalRepository{
        return GoalRepositoryImpl(db.goalDao)
    }

    @Provides
    @Singleton
    fun initiateHistoryRepository(db:IFitDatabase):HistoryRepository{
        return HistoryRepositoryImpl(db.historyDao)
    }

    @Provides
    @Singleton
    fun initiateStatusStackRepository(db:IFitDatabase):StatusStackRepository{
        return StatusStackRepositoryImpl(db.statusStackDao)
    }

    @Provides
    @Singleton
    fun initiateProcesses(repository: GoalRepository): GoalProcessManager{
        return GoalProcessManager(
            listGoal = GetGoalsProcess(repository),
            addGoal = AddGoalProcess(repository),
            deleteGoal = DeleteGoalProcess(repository),
            getActiveGoal = GetActiveGoalProcess(repository),
            getGoalById = GetGoalByIdProcess(repository),
            getGoalByName = GetGoalByNameProcess(repository)
        )
    }

    @Provides
    @Singleton
    fun initiateStatusProcesses(repository: StatusStackRepository): StatusProcessManager{
        return StatusProcessManager(
            addStatusStackProcess = AddStatusStackProcess(repository),
            getElementFromStack = GetElementFromStack(repository),
            deleteStatusStackProcess = DeleteStatusStackProcess(repository)
        )
    }

    @Provides
    @Singleton
    fun initiateHistoryProcesses(repository: HistoryRepository): HistoryProcessManager{
        return HistoryProcessManager(
            getHistoryRecordsProcess = GetHistoryRecordsProcess(repository),
            addHistoryProcess = AddHistoryProcess(repository),
            getHistoryRecordToday = GetHistoryRecordToday(repository),
            getHistoryRecordById = GetHistoryByIdProcess(repository),
            clearHistory = DeleteHistoryProcess(repository)
        )
    }

    @Provides
    @Singleton
    fun initiatePreferences(@ApplicationContext context:Context) : PreferenceManager{
        return PreferenceManager(context)
    }
}