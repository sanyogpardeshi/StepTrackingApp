package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.HistoryRepository
import com.example.ifit.IFitApp.Domain.Model.History

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH HISTORY BY ID */

class GetHistoryByIdProcess(val repository: HistoryRepository) {

    suspend operator fun invoke(id:Int) : History? {
        return repository.getHistoryById(id)
    }
}