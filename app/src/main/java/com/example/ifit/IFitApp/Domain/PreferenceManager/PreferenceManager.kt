package com.example.ifit.IFitApp.Domain.PreferenceManager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

/* PREFERENCE DATA STORE MANAGER CLASS */
/* THIS CLASS IS RESPONSABLE FOR STORE USER PREFERENCES IN MEMORY */
/* BASICS OF THIS CLASS
*
*  IN THE BEGINNING WE FETCH THE INSTANCE OF USER PREFERENCES
*  IN GENERAL WE HAVE 2 METHODS; ONE FOR SET VALUES AND ANOTHER FOR GET VALUES
*
*  LOGIS IS SIMPLE. PREFERENCE DATA STORE WORKS WITH KEY VALUE PAIRS. WE CAN SET AND GET VALUES WITH USING THESE KEYS.
*
*  */

class PreferenceManager(val context:Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_PREFERENCES")

    companion object {
        val GOAL_PREFERENCE = booleanPreferencesKey("GOAL_EDITABLE")
        val HISTORY_RECORD_PREFERENCE = booleanPreferencesKey("HISTORY_RECORD_ENABLE_STATUS")
    }


    suspend fun changePreferencesDataStore(preferences:com.example.ifit.IFitApp.Domain.Model.Preferences){
        context.dataStore.edit {
            it[GOAL_PREFERENCE] = preferences.goalEditable
            it[HISTORY_RECORD_PREFERENCE] = preferences.historyRecordMode
        }
    }

    suspend fun getFromDataStore() = context.dataStore.data.map {
        com.example.ifit.IFitApp.Domain.Model.Preferences (
                goalEditable = it[GOAL_PREFERENCE]?:true,
                historyRecordMode = it[HISTORY_RECORD_PREFERENCE]?:true
                )
    }

}