package com.example.ifit.IFitApp.Presentation.ViewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifit.IFitApp.Domain.Manager.HistoryProcessManager
import com.example.ifit.IFitApp.Domain.Manager.GoalProcessManager
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.History
import com.example.ifit.IFitApp.Presentation.States.GoalListOrder
import com.example.ifit.IFitApp.Presentation.States.HistoryEvents
import com.example.ifit.IFitApp.Presentation.States.HistoryStates
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val manager : HistoryProcessManager,
    private val goalManager: GoalProcessManager,
    private val preferenceManager: com.example.ifit.IFitApp.Domain.PreferenceManager.PreferenceManager,
    @ApplicationContext context : Context
) : ViewModel() {

    private val _historyStates = mutableStateOf(HistoryStates())
    val historyStates : State<HistoryStates> = _historyStates

    private var historyRecord : History? = null

    private val _eventFlow = MutableSharedFlow<HistoryViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    /* CHECK IF HISTORY MODE IS ACTIVE OR NOT AND FETCH THE NECESSARY DATA */
    init {
        viewModelScope.launch {
            preferenceManager.getFromDataStore().collect{
                withContext(Dispatchers.Main){
                    _historyStates.value = historyStates.value.copy(
                        isGoalEditable = it.historyRecordMode
                    )
                }
            }
        }
        listRecords(GoalListOrder.listAllTime())
        fetchGoals()
    }

    /* DETECTING USER ACTIVITIES IN VIEW AND PERFORM RELATED LOGICAL OPERATIONS
    *
    * CHANGE ORDER TYPE    -> CHANGES THE STATE OF ORDER TYPE SO VIEW CAN RECOMPOSE AFTER USER CHANGE LIST OPTIONS ( ALL/SPESIFIC DATE )
    * EDIT HISTORY         -> OPENS EDIT HISTORY POPUP
    * CLOSE POPUP          -> CLOSES EDIT HISTORY POPUP
    * STEP ENTERED         -> UPDATE STATES WHEN USER EDITING THE HISTORY SO COMPOSE CAN RECOMPOSE  --STEP COUNT UPDATE
    * CHOSEN GOAL          -> UPDATE STATES WHEN USER EDITING THE HISTORY SO COMPOSE CAN RECOMPOSE  --ACTIVE GOAL UPDATE
    * ADD HISTORY NEW      -> EDITS THE SELECTED RECORD WITH USER CHOICES AND UPDATES STATES
    * GET RECORD FOR DATE  -> GETS RELATED RECORD FOR SELECTED DATE IF EXISTS
    * CLEAR HISTORY        -> CLEAR ALL HISTORICAL DATA
    *
    *  */
    fun onInteract(historyEvents : HistoryEvents) {
        when(historyEvents) {
            is HistoryEvents.changeOrderType -> {
                _historyStates.value = historyStates.value.copy(goalOrder = historyEvents.orderType)
                viewModelScope.launch {
                    _historyStates.value = _historyStates.value.copy(
                        goalOrder = historyEvents.orderType
                    )
                    _historyStates.value = _historyStates.value.copy(
                        date = ""
                    )
                    listRecords(GoalListOrder.listAllTime())
                }
            }
            is HistoryEvents.editHistory -> {
                viewModelScope.launch {
                    try{
                        historyRecord  = historyEvents.history

                        if (historyRecord != null) {
                            _historyStates.value = historyStates.value.copy(
                                stepEntered = historyRecord!!.totalSteps.toString()
                            )
                            _historyStates.value = historyStates.value.copy(
                                chosenGoal = historyRecord!!.goalName
                            )
                        }
                        _historyStates.value = _historyStates.value.copy(
                            editHistoryRecordPopup = !historyStates.value.editHistoryRecordPopup
                        )
                    }catch (e:java.lang.Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }
                }
            }
            is HistoryEvents.closePopUp -> {
                _historyStates.value = historyStates.value.copy(
                    editHistoryRecordPopup = !historyStates.value.editHistoryRecordPopup
                )
                _historyStates.value = historyStates.value.copy(
                    stepEntered = ""
                )
            }
            is HistoryEvents.stepsEntered -> {
                _historyStates.value = _historyStates.value.copy(
                    stepEntered = historyEvents.step
                )
            }
            is HistoryEvents.chosenGoal -> {
                _historyStates.value = _historyStates.value.copy(
                    chosenGoal = historyEvents.goal
                )
            }
            is HistoryEvents.addHistoryNew -> {
                viewModelScope.launch {
                    try {
                        if(_historyStates.value.isGoalEditable){
                            val newStepCount = _historyStates.value.stepEntered.toInt()
                            val newGoal = _historyStates.value.chosenGoal
                            var newGoalId = historyRecord?.goalId

                            if(newGoal!=null||newGoal!=""){
                                newGoalId = goalManager.getGoalByName(newGoal)?.id
                            }

                            if(historyRecord!=null && newGoalId!=null){
                                val history : History = History(
                                    goalName = newGoal,
                                    id = historyRecord!!.id,
                                    day = historyRecord!!.day,
                                    date = historyRecord!!.date,
                                    totalSteps = newStepCount,
                                    goalId = newGoalId,
                                    month = historyRecord!!.month,
                                    year = historyRecord!!.year
                                )
                                manager.addHistoryProcess(history)
                            }
                            _historyStates.value = historyStates.value.copy(
                                editHistoryRecordPopup = !historyStates.value.editHistoryRecordPopup
                            )
                            _historyStates.value = historyStates.value.copy(
                                stepEntered = ""
                            )
                            listRecords(GoalListOrder.listAllTime())
                        }
                    }catch (e:java.lang.Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }
                }
            }
            is HistoryEvents.getRecordForDate -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val month = historyEvents.month.plus(1)
                        val history: History? = manager.getHistoryRecordToday(historyEvents.day,month,historyEvents.year)
                        if(history!=null){
                            _historyStates.value = historyStates.value.copy(
                                goals = emptyList(),
                                goal = history,
                                goalOrder = GoalListOrder.getRecordForDay(),
                                date = "${historyEvents.day}/${month}/${historyEvents.year}"
                            )
                        }else{
                            _historyStates.value = historyStates.value.copy(
                                goals = emptyList(),
                                goal = null,
                                goalOrder = GoalListOrder.getRecordForDay(),
                                date = "${historyEvents.day}/${month}/${historyEvents.year}"
                            )
                        }
                    }catch (e:java.lang.Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }
                }
            }
            is HistoryEvents.clearHistory -> {
                CoroutineScope(Dispatchers.IO).launch {
                    manager.clearHistory()
                }
            }
        }
    }

    /************************************************************************************************************************************************************/
    /* METHODS THAT NECESSARY TO PERFORM LOGICAL OPERATIONS FOR GOAL VIEW MODEL */
    /* EACH METHOD HAS IT'S OWN EXPLANATION ON TOP OF IT */
    /* AIM: PROVIDE MODULARITY, AVOID WRITE SAME CODES MULTIPLE TIME */
    /************************************************************************************************************************************************************/

    /* FETCH ALL RECORDS IF EXIST AND UPDATE THE STATE SO VIEW CAN RECOMPOSE */
    private fun listRecords(orderType : GoalListOrder){
            val list : Flow<List<History>> =  manager.getHistoryRecordsProcess()
            list.onEach {records ->
                _historyStates.value = historyStates.value.copy(
                    goals = records,
                    goalOrder = orderType,
                )
            }.launchIn(viewModelScope)
    }

    /* FETCH ALL GOALS SO USER CAN CHOOSE WHEN USER WANTS TO CHANGE ACTIVE GOAL OF HISTORY */
    private fun fetchGoals(){
        val goals: Flow<List<Goal>> = goalManager.listGoal()
        goals.onEach { goals ->
            _historyStates.value = historyStates.value.copy(
                actualGoals = goals
            )
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        object ReloadAction: UiEvent()
        object UnknownError: UiEvent()
    }
}