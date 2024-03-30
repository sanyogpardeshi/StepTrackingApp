package com.example.ifit.IFitApp.Presentation.States

sealed class HomeEvents {
    data class  stepsEntered(val value:String) : HomeEvents()

    object openAddStepPopup : HomeEvents()
    object closeAddStepPopup : HomeEvents()
    object addStep : HomeEvents()

}
