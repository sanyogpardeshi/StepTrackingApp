package com.example.ifit.IFitApp.Domain.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow
import java.util.*

/* ENTITY CLASS REPRESENTS HISTORY */

@Entity
data class History (
        val goalId : Int,
        val goalName : String,
        val totalSteps: Int,
        val date : Date,
        val day : Int,
        val month : Int,
        val year : Int,
        @PrimaryKey val id:Int? = null
)
