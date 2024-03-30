package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.HistoryRepository
import com.example.ifit.IFitApp.Domain.Model.History
import kotlinx.coroutines.flow.Flow

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH HISTORY RECORD FOR GIVEN DATE */

class GetHistoryRecordToday(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(day:Int,month:Int,year:Int) : History? {
        return historyRepository.getHistoryRecordForToday(day,month,year)
    }
}