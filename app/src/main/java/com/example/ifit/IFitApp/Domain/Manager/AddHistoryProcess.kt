package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.HistoryRepository
import com.example.ifit.IFitApp.Domain.Model.History

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* ADD HISTORY TO DB */

class AddHistoryProcess(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(history:History){
        historyRepository.addHistory(history)
    }
}