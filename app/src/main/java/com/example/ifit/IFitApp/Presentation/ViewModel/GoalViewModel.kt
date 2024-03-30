package com.example.ifit.IFitApp.Presentation.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifit.IFitApp.Domain.Error.ErrorTypes
import com.example.ifit.IFitApp.Domain.Manager.GoalProcessManager
import com.example.ifit.IFitApp.Domain.Manager.StatusProcessManager
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.StatusStack
import com.example.ifit.IFitApp.Domain.PreferenceManager.PreferenceManager
import com.example.ifit.IFitApp.Presentation.States.GoalEvents
import com.example.ifit.IFitApp.Presentation.States.GoalStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val manager : GoalProcessManager,
    private val preferenceManager: PreferenceManager,
    private val statusManager: StatusProcessManager
    ) : ViewModel(){

    private val _goalStates  = mutableStateOf(GoalStates())
    val goalStates : State<GoalStates> = _goalStates

    private val _toggledCards = MutableStateFlow(listOf<Int>())
    val toggledCards : StateFlow<List<Int>> get() = _toggledCards

    private val _longPressedCards = MutableStateFlow(listOf<Int>())
    val longPressedCards : StateFlow<List<Int>> get() = _longPressedCards

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var restoredGoal : Goal? = null
    private var editingGoalId : Int? = null

    /* CHECK IF GOAL IS EDITABLE WHEN SCREEN STARTED USING PREFERENCE MANAGER THEN FETCH GOALS*/
    init {
        viewModelScope.launch {
            preferenceManager.getFromDataStore().collect{
                withContext(Dispatchers.Main){
                    _goalStates.value = _goalStates.value.copy(
                        isGoalEditable = it.goalEditable
                    )
                }
            }
        }
        getGoals()
    }

    /* DETECTING USER ACTIVITIES IN VIEW AND PERFORM RELATED LOGICAL OPERATIONS
    *
    * SHOW DETAILS = OPENS THE GOAL DETAILS
    * OPEN ADD GOAL POPUP/ CLOSE ADD GOAL POPUP = OPENS/CLOSES THE POP UP OF ADD GOAL FORM
    * TITLE ENTERED/COUNT ENTERED = UPDATES GOAL NAME AND STEP COUNT WHEN USER SUBMIT A FORM
    * ADD GOAL = ADDING GOAL TO DB
    * LONG PRESS MENU = ARRANGES ELEMENTS IN THE LONG PRESS MENU LIST. THERE SHOULD NOT BE MORE THAN ONE ELEMENT LONG PRESSED AT THE SAME TIME
    * DELETE GOAL = DELETES GOAL AND ALSO PROTECT THAT GOAL IN A VARIABLE SO USER CAN RESTORE IT
    * RESTORE GOAL = RESTORES THE DELETED GOAL
    * ACTIVATE GOAL = CHANGES THE ACTIVE STATUS OF THE GOAL. IF THERE IS ANOTHER GOAL ACTIVATED IT AUTOMATICALLY DEACTIVES IT FIRST.
    * EDIT GOAL = PERFORM LOGIC FOR EDITING GOAL
    *
    *  */
    @RequiresApi(Build.VERSION_CODES.O)
    fun onInteract(goalEvents: GoalEvents){
        when(goalEvents){
            is GoalEvents.getGoals -> {
                getGoals()
            }
            is GoalEvents.ShowDetails -> {
                _toggledCards.value = _toggledCards.value.toMutableList().also {list->
                    if (goalEvents.id != null) {
                        if(list.contains(goalEvents.id)){
                            list.remove(goalEvents.id)
                        }else{
                            list.clear()
                            list.add(goalEvents.id)
                        }
                    }
                }
            }
            is GoalEvents.openAddGoalPopup -> {
                viewModelScope.launch {
                    _goalStates.value = _goalStates.value.copy(addGoalPopUpActive = !goalStates.value.addGoalPopUpActive)
                    editingGoalId = null
                }

            }
            is GoalEvents.closeAddGoalPopup -> {
                viewModelScope.launch {
                    _goalStates.value = _goalStates.value.copy(addGoalPopUpActive = !goalStates.value.addGoalPopUpActive)
                    editingGoalId = null
                    _eventFlow.emit(UiEvent.GoalAction)
                }
            }
            is GoalEvents.titleEntered -> {
                _goalStates.value = _goalStates.value.copy(title = goalEvents.value)
            }
            is GoalEvents.countEntered -> {
                _goalStates.value = _goalStates.value.copy(step = goalEvents.value)
            }
            is GoalEvents.addGoal -> {
                val willBeActivated = false

                viewModelScope.launch {
                    var goal: Goal? =null
                    try{
                        if(editingGoalId!=null){
                            goal = Goal(
                                title =  _goalStates.value.title,
                                stepCount = _goalStates.value.step.toInt(),
                                isActive = willBeActivated,
                                timestamp = Date(System.currentTimeMillis()),
                                id = editingGoalId
                            )
                            manager.addGoal(goal,true)
                        }else{
                            goal = Goal(
                                title = _goalStates.value.title,
                                stepCount = _goalStates.value.step.toInt(),
                                isActive = willBeActivated,
                                timestamp = Date(System.currentTimeMillis()),
                            )
                            manager.addGoal(goal,false)
                        }
                        _eventFlow.emit(UiEvent.GoalAction)
                    }catch (e: ErrorTypes.ExistingGoalNameException){
                        _eventFlow.emit(UiEvent.ExistingGoalAction)
                    }
                }
            }
            is GoalEvents.LongPressMenu -> {
                _longPressedCards.value = _longPressedCards.value.toMutableList().also {list->
                    if (goalEvents.id != null) {
                        if(list.contains(goalEvents.id)){
                            list.remove(goalEvents.id)
                        }else{
                            list.clear()
                            list.add(goalEvents.id)
                        }
                    }
                }
            }
            is GoalEvents.deleteGoal -> {
                viewModelScope.launch {
                    try{
                        restoredGoal = goalEvents.goal
                        _toggledCards.value = _toggledCards.value.toMutableList().also {list->
                            if (restoredGoal?.id != null) {
                                if (list.contains(restoredGoal?.id)) {
                                    list.remove(restoredGoal?.id)
                                }
                            }}
                        manager.deleteGoal(goalEvents.goal)
                    }catch (e : Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }
                    _eventFlow.emit(UiEvent.DeletedGoal)
            }}
            is GoalEvents.restoreGoal -> {
                viewModelScope.launch {
                    if (restoredGoal != null) {
                        manager.addGoal(restoredGoal!!,false)
                    }
                    restoredGoal = null
                    getGoals()
                }
            }
            is GoalEvents.activateGoal -> {
                viewModelScope.launch {
                    try{
                        val oldActiveGoal:Goal? = manager.getActiveGoal.invoke(true)
                        if (oldActiveGoal != null) {
                            val deActivatedGoal:Goal = changeGoalActiveStatus(oldActiveGoal,false)
                            transferStepOneToAnother(deActivatedGoal.id!!,goalEvents.goal.id!!)

                            manager.addGoal(deActivatedGoal,true)
                        }
                        val goal:Goal  = changeGoalActiveStatus(goalEvents.goal,true)
                        manager.addGoal(goal,true)
                    }catch (e: Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }
                    _eventFlow.emit(UiEvent.GoalAction)
                }
            }
            is GoalEvents.editGoal ->{
                editingGoalId = goalEvents.goal.id
                viewModelScope.launch {
                    val goal : Goal = manager.getGoalById(goalEvents.goal.id!!)
                    _goalStates.value = _goalStates.value.copy(title = goal.title)
                    _goalStates.value = _goalStates.value.copy(step = goal.stepCount.toString())
                    _goalStates.value = _goalStates.value.copy(addGoalPopUpActive = !goalStates.value.addGoalPopUpActive)
                }
            }
        }
    }


    /************************************************************************************************************************************************************/
    /* METHODS THAT NECESSARY TO PERFORM LOGICAL OPERATIONS FOR GOAL VIEW MODEL */
    /* EACH METHOD HAS IT'S OWN EXPLANATION ON TOP OF IT */
    /* AIM: PROVIDE MODULARITY, AVOID WRITE SAME CODES MULTIPLE TIME */
    /************************************************************************************************************************************************************/

    /* METHOD FOR FETCH ALL GOALS FROM DATABASE. AFTER FETCH DONE, STATE IS UPDATED */
    private fun getGoals() {
         manager.listGoal().onEach { goals ->
             _goalStates.value = goalStates.value.copy(
                goals = goals
            )
        }.launchIn(viewModelScope)
    }

    /* METHOD FOR CHANGE GOAL'S ACTIVE STATUS */
    private fun changeGoalActiveStatus(goal:Goal,isActivated:Boolean) : Goal{
        val goal = Goal(
            title = goal.title,
            stepCount = goal.stepCount,
            isActive = isActivated,
            timestamp = goal.timestamp,
            id = goal.id
        )
        return goal
    }

    /* METHOD FOR TRANSFER STEP COUNT FROM ONE GOAL TO ANOTHER WHEN ACTIVE GOAL CHANGE */
    private suspend fun transferStepOneToAnother(deActivedGoalId:Int,activatedGoalId:Int){
        val date = Date(System.currentTimeMillis())
        var deActivatedGoalStepCount: Int = 0

        val deActivatedStatus = statusManager.getElementFromStack(deActivedGoalId)
        if(deActivatedStatus!=null && deActivatedStatus.date.day==date.day && deActivatedStatus.date.month==date.month && deActivatedStatus.date.year==date.year){
            deActivatedGoalStepCount = deActivatedStatus.stepCount
            statusManager.deleteStatusStackProcess(deActivatedStatus)
        }
        val updatedNewStatus = StatusStack(
            goalId = activatedGoalId,
            date = date,
            stepCount = deActivatedGoalStepCount
        )
        statusManager.addStatusStackProcess(updatedNewStatus)
    }

    sealed class UiEvent {
        object GoalAction: UiEvent()
        object ExistingGoalAction: UiEvent()
        object DeletedGoal: UiEvent()
        object UnknownError: UiEvent()
    }
}
