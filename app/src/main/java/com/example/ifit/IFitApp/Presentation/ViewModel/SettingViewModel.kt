package com.example.ifit.IFitApp.Presentation.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifit.IFitApp.Domain.Model.Preferences
import com.example.ifit.IFitApp.Domain.PreferenceManager.PreferenceManager
import com.example.ifit.IFitApp.Presentation.States.SettingEvents
import com.example.ifit.IFitApp.Presentation.States.SettingsStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor (
    private val preferenceManager: PreferenceManager
): ViewModel(){

    private val _settingStates  = mutableStateOf(SettingsStates())
    val settingStates : State<SettingsStates> = _settingStates

    init {
        viewModelScope.launch {
            preferenceManager.getFromDataStore().collect{
                withContext(Dispatchers.Main){
                    _settingStates.value = _settingStates.value.copy(
                        isGoalEditable = it.goalEditable,
                        isHistoryRecordable = it.historyRecordMode
                    )
                }
            }
        }
    }

    fun onInteract(settingEvents: SettingEvents){
        when(settingEvents) {
            is SettingEvents.changeGoalEditableMode -> {
                viewModelScope.launch {
                    _settingStates.value = _settingStates.value.copy(
                        isGoalEditable = !settingEvents.chGoal
                    )
                    val preferences: Preferences  = Preferences(
                        goalEditable = !settingEvents.chGoal,
                        historyRecordMode = settingStates.value.isHistoryRecordable
                    )
                    preferenceManager.changePreferencesDataStore(preferences)

                }
            }
            is SettingEvents.changeHistoryRecordMode -> {
                viewModelScope.launch {
                    _settingStates.value = _settingStates.value.copy(
                        isHistoryRecordable = !settingEvents.chHist
                    )
                    val preferences: Preferences  = Preferences(
                        goalEditable = settingStates.value.isGoalEditable,
                        historyRecordMode = !settingEvents.chHist
                    )
                    preferenceManager.changePreferencesDataStore(preferences)
                }
            }
        }
    }
}