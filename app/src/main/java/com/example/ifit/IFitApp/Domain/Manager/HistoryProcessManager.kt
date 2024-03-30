package com.example.ifit.IFitApp.Domain.Manager

/* MANAGER CLASS THAT COLLECTS ALL MANAGER MEMBERS */

data class HistoryProcessManager(
    val getHistoryRecordsProcess: GetHistoryRecordsProcess,
    val addHistoryProcess: AddHistoryProcess,
    val getHistoryRecordToday : GetHistoryRecordToday,
    val getHistoryRecordById : GetHistoryByIdProcess,
    val clearHistory : DeleteHistoryProcess
)
