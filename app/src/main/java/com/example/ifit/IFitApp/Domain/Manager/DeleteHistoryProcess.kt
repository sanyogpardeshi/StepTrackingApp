package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.HistoryRepository

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* DELETE HISTORY RECORDS */

class DeleteHistoryProcess(private val historyRepository: HistoryRepository) {

    suspend operator fun invoke(){
        historyRepository.deleteHistory()
    }
}