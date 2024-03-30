package com.example.ifit.IFitApp.Domain.Manager

import com.example.ifit.IFitApp.Data.Repository.StatusStackRepository
import com.example.ifit.IFitApp.Domain.Model.StatusStack

/* MANAGER MEMBER THAT COMMUNICATE WITH DATA LAYER */
/* DELETE SPECIFIED STACK ELEMENT */

class DeleteStatusStackProcess(private val statusStackRepository: StatusStackRepository) {

    suspend operator fun invoke(element:StatusStack){
        statusStackRepository.deleteElementFromStack(element)
    }
}