package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.HistoryRepository
import com.example.ifit.IFitApp.Domain.Model.History
import com.example.ifit.IFitApp.Presentation.States.GoalListOrder
import kotlinx.coroutines.flow.Flow
import java.sql.Date

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH HISTORY RECORDS */

class GetHistoryRecordsProcess(private val historyRepository: HistoryRepository) {

    operator fun invoke() : Flow<List<History>> {
        return historyRepository.getHistoryRecords()

    }
}