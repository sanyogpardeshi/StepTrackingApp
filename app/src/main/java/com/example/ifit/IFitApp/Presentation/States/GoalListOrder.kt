package com.example.ifit.IFitApp.Presentation.States

sealed class GoalListOrder(){
    class listAllTime() : GoalListOrder()
    class getRecordForDay() : GoalListOrder()
}
