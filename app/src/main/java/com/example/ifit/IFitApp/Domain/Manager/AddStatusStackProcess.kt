package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.StatusStackRepository
import com.example.ifit.IFitApp.Domain.Model.StatusStack

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* ADD STATUS TO DB */

class AddStatusStackProcess(private val statusStackRepository: StatusStackRepository) {

    suspend operator fun invoke(statusStack: StatusStack){
        statusStackRepository.addStatusElementToStack(statusStack)
    }
}