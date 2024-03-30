package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.DataSource.StatusStackDao
import com.example.ifit.IFitApp.Data.Repository.StatusStackRepository
import com.example.ifit.IFitApp.Domain.Model.StatusStack

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* FETCH SPESIFIED STATUS ELEMENT */

class GetElementFromStack(private val statusStackRepository: StatusStackRepository) {

    suspend operator fun invoke(id:Int) : StatusStack?{
        return statusStackRepository.getStatusStackElementById(id)
    }
}