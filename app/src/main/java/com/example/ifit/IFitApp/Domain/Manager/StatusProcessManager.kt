package com.example.ifit.IFitApp.Domain.Manager

/* MANAGER CLASS THAT COLLECTS ALL MANAGER MEMBERS */

data class StatusProcessManager(
    val addStatusStackProcess: AddStatusStackProcess,
    val getElementFromStack: GetElementFromStack,
    val deleteStatusStackProcess: DeleteStatusStackProcess
)