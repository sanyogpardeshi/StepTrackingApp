package com.example.ifit.IFitApp.Presentation.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifit.IFitApp.Domain.Manager.HistoryProcessManager
import com.example.ifit.IFitApp.Domain.Manager.GoalProcessManager
import com.example.ifit.IFitApp.Domain.Manager.StatusProcessManager
import com.example.ifit.IFitApp.Domain.Model.Goal
import com.example.ifit.IFitApp.Domain.Model.History
import com.example.ifit.IFitApp.Domain.Model.StatusStack
import com.example.ifit.IFitApp.Domain.PreferenceManager.PreferenceManager
import com.example.ifit.IFitApp.Presentation.States.HomeEvents
import com.example.ifit.IFitApp.Presentation.States.HomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.sql.Date
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manager: StatusProcessManager,
    private val historyManager : HistoryProcessManager,
    private val goalManager: GoalProcessManager,
    private val preferenceManager: PreferenceManager
    ): ViewModel() {

    private val _homeStates = mutableStateOf(HomeStates())
    val homeStates : State<HomeStates> = _homeStates

    var activeGoal : Goal? = null
    val date: Date = Date(System.currentTimeMillis())

    private val _eventFlow = MutableSharedFlow<HomeViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    /* FETCH ACTIVE GOAL IF THERE IS ONE AND FILL THE TEXT FIELDS IN THE SCREEN WITH RELAVANT DATA. */
    init{
        viewModelScope.launch {
            try {
                activeGoal = goalManager.getActiveGoal(true)
                _homeStates.value = _homeStates.value.copy(
                    activeGoal = activeGoal
                )
                if ((activeGoal != null)) {
                    val statusStack = activeGoal!!.id?.let { manager.getElementFromStack(it) }
                    _homeStates.value = _homeStates.value.copy(
                        goalStepCount = activeGoal!!.stepCount.toString(),
                        goalName = activeGoal!!.title,

                        )
                    if (statusStack != null ) {
                        val isToday = checkForDateIsToday(statusStack,date)
                        val _goalStepCount = activeGoal!!.stepCount
                        val _done = statusStack.stepCount
                        var _left = (_goalStepCount.minus(_done))
                        if(_left<0){
                            _left = 0
                        }

                        if(isToday){
                            val _percentage = calculatePercentage(statusStack.stepCount.toFloat())
                            _homeStates.value = _homeStates.value.copy(
                                totalSteps = statusStack.stepCount.toString(),
                                percentage = _percentage,
                                done = statusStack.stepCount.toString(),
                                left = _left.toString()
                            )
                        }else{
                            _homeStates.value = _homeStates.value.copy(
                                totalSteps = statusStack.stepCount.toString(),
                                percentage = 0f,
                                done = "0",
                                left = _left.toString()
                            )
                        }
                    }
                }
                preferenceManager.getFromDataStore().collect{
                    withContext(Dispatchers.Main){
                        _homeStates.value = _homeStates.value.copy(
                            isHistoryModeOn = it.historyRecordMode
                        )
                    }
                }
            }catch (e:Exception){
                _eventFlow.emit(UiEvent.UnknownError)
            }
        }
    }

    /* DETECTING USER ACTIVITIES IN VIEW AND PERFORM RELATED LOGICAL OPERATIONS
    *
    * ADD STEP              -> CHECKS IF THERE IS A ACTIVE GOAL. IF THERE IS THEN IT ADDS THE STEP, CALCULATE PERCENTAGE AND UPDATE STATES.
    *                          IF STEP IS SUBMITTED,SIMILTINOUSLY IT UPDATE HISTORY BY CHECKING HISTORY MODE. ALSO KEEP VALUE IN A STACK SO WE CAN TRACK
    *
    *OPEN/CLOSE STEP POPUP  -> OPENS OR CLOSES TO ADD STEP POP UP.
    *STEPS ENTERED          -> KEEP TRACKING THE STEPS THAT USER ENTERS IN UI AND UPDATE STATE.
    *
    * */

    @RequiresApi(Build.VERSION_CODES.O)
    fun onInteract(homeEvents: HomeEvents){
        when(homeEvents) {
            is HomeEvents.addStep -> {
                val localDate: LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val year: Int = localDate.year
                val month: Int = localDate.monthValue
                val day: Int = localDate.dayOfMonth

                var statusStack : StatusStack? = null
                var historyRecordToday : History? = null
                var finalStep:Int = 0
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        if(activeGoal!=null){
                            historyRecordToday = historyManager.getHistoryRecordToday(day,month,year)
                            //Is there any record saved into history table with that goal date?
                            //If there is a record, fetch it first and update step counts.
                            if(_homeStates.value.isHistoryModeOn){
                                if(historyRecordToday!=null){
                                    val recordStepCount = historyRecordToday!!.totalSteps
                                    val updatedStepCount = recordStepCount.plus(_homeStates.value.stepEntered.toInt())
                                    val historyRecord : History = activeGoal!!.id?.let {
                                        History(
                                            goalId = it,
                                            goalName = activeGoal!!.title,
                                            totalSteps = updatedStepCount,
                                            date = Date(System.currentTimeMillis()),
                                            day = day,
                                            month = month,
                                            year = year,
                                            id = historyRecordToday!!.id
                                        )
                                    }!!
                                    historyManager.addHistoryProcess(historyRecord)
                                }
                                //If there is no record exist about that date, create new one.
                                else{
                                    val historyRecord : History = activeGoal!!.id?.let {
                                        History(
                                            goalId = it,
                                            goalName = activeGoal!!.title,
                                            totalSteps = _homeStates.value.stepEntered.toInt(),
                                            date = Date(System.currentTimeMillis()),
                                            day = day,
                                            month = month,
                                            year = year,
                                        )
                                    }!!
                                    historyManager.addHistoryProcess(historyRecord)
                                }
                            }
                            statusStack = activeGoal!!.id?.let { manager.getElementFromStack(it) }
                            //if there is a record about that goal.
                            if(statusStack!=null){
                                val isToday = checkForDateIsToday(statusStack!!,date)
                                if(isToday){
                                    finalStep = statusStack!!.stepCount.plus(_homeStates.value.stepEntered.toInt())
                                    val _percentage = calculatePercentage(finalStep.toFloat())
                                    _homeStates.value = _homeStates.value.copy(
                                        totalSteps = finalStep.toString(),
                                        percentage = _percentage,
                                        done = finalStep.toString()
                                    )
                                }else{
                                    finalStep = 0
                                    _homeStates.value = _homeStates.value.copy(
                                        totalSteps = finalStep.toString(),
                                        percentage = 0f,
                                        done = finalStep.toString()
                                    )
                                }
                                statusStack = activeGoal!!.id?.let {
                                    StatusStack(
                                        goalId = it,
                                        stepCount = finalStep,
                                        date = Date(System.currentTimeMillis()),
                                        id = statusStack!!.id
                                    )
                                }
                            }
                            //if this is the first record to stack about the goal.
                            else{
                                finalStep = _homeStates.value.stepEntered.toInt()
                                var _percentage = calculatePercentage(finalStep.toFloat())
                                _homeStates.value = _homeStates.value.copy(
                                    totalSteps = finalStep.toString(),
                                    percentage = _percentage,
                                    done = finalStep.toString()
                                )
                                statusStack = activeGoal!!.id?.let {
                                    StatusStack(
                                        goalId = it,
                                        stepCount = finalStep,
                                        date = Date(System.currentTimeMillis()),
                                    )
                                }
                            }
                        }
                        statusStack?.let { manager.addStatusStackProcess(it) }
                        val _goalStepCount = _homeStates.value.goalStepCount
                        val _done = _homeStates.value.done
                        var _left = (_goalStepCount.toInt().minus(_done.toInt()))
                        if(_left<0){
                            _left = 0
                        }
                        _homeStates.value = _homeStates.value.copy(
                            done = finalStep.toString(),
                            left = _left.toString(),
                            stepEntered = "",
                            openAddStepPopUp = !homeStates.value.openAddStepPopUp
                        )
                    }catch (e:Exception){
                        _eventFlow.emit(UiEvent.UnknownError)
                    }

                }
            }
            is HomeEvents.openAddStepPopup -> {
                _homeStates.value = _homeStates.value.copy(
                    stepEntered = "",
                    openAddStepPopUp = !homeStates.value.openAddStepPopUp,
                )
            }
            is HomeEvents.closeAddStepPopup -> {
                _homeStates.value = _homeStates.value.copy(
                    openAddStepPopUp = !homeStates.value.openAddStepPopUp,
                )
            }
            is HomeEvents.stepsEntered -> {
                _homeStates.value = _homeStates.value.copy(
                    stepEntered = homeEvents.value,
                )
            }
        }
    }

    /************************************************************************************************************************************************************/
    /* METHODS THAT NECESSARY TO PERFORM LOGICAL OPERATIONS FOR GOAL VIEW MODEL */
    /* EACH METHOD HAS IT'S OWN EXPLANATION ON TOP OF IT */
    /* AIM: PROVIDE MODULARITY, AVOID WRITE SAME CODES MULTIPLE TIME */
    /************************************************************************************************************************************************************/

    /* CALCULATES THE PROGRESS AND RETURN A FLOAT VALUE  */
    fun calculatePercentage(totalStep : Float) : Float {
        val totalStepsAsInt: Float = totalStep
        val goalStepsAsInt: Float = _homeStates.value.goalStepCount.toFloat()
        var perc = totalStepsAsInt / goalStepsAsInt
        if((totalStepsAsInt / goalStepsAsInt)>1f){
            perc = 1f
        }
        return perc
    }

    /* CHECKS DATE, WE SHOULD PREVENT ADD STEP PROCESS IF DATE IS NOT MATCH WITH THE DATE HELD IN STACK */
    fun checkForDateIsToday(statusStack : StatusStack,date : java.util.Date) : Boolean {
        var isToday = true
        val date:java.util.Date = java.util.Date(System.currentTimeMillis())
        val day = date.day
        val month = date.month
        val year = date.year
        if(!(statusStack.date.day==day && statusStack.date.month==month && statusStack.date.year==year)){
            isToday = false
        }
        return isToday
    }

    sealed class UiEvent {
        object UnknownError: UiEvent()
    }
}