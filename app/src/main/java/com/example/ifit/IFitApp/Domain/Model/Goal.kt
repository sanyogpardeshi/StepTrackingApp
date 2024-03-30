package com.example.ifit.IFitApp.Domain.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/* ENTITY CLASS REPRESENTS GOAL */

@Entity
data class Goal(
    val title:String,
    val stepCount:Int,
    val isActive:Boolean,
    val timestamp: Date,
    @PrimaryKey val id:Int? = null
)

