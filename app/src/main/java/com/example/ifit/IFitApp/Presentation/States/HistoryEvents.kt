package com.example.ifit.IFitApp.Presentation.States

import com.example.ifit.IFitApp.Domain.Model.History
import java.time.Month

sealed class HistoryEvents{
    data class changeOrderType(val orderType : GoalListOrder) : HistoryEvents()
    data class getRecordForDate(val day : Int,val month : Int,val year : Int) : HistoryEvents()
    data class editHistory(val history:History) : HistoryEvents()
    data class stepsEntered(val step:String) : HistoryEvents()
    data class chosenGoal(val goal:String) : HistoryEvents()

    object closePopUp : HistoryEvents()
    object addHistoryNew : HistoryEvents()
    object clearHistory : HistoryEvents()

}
