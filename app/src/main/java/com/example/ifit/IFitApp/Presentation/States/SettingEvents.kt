package com.example.ifit.IFitApp.Presentation.States

sealed class SettingEvents{
    class changeGoalEditableMode(val chGoal:Boolean) : SettingEvents()
    class changeHistoryRecordMode(val chHist: Boolean) : SettingEvents()
}
