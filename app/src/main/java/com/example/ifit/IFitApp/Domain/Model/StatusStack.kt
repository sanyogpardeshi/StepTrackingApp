package com.example.ifit.IFitApp.Domain.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/* ENTITY CLASS REPRESENTS STATUS STACKS */

@Entity
data class StatusStack(
    val goalId : Int,
    val stepCount : Int,
    val date : Date,
    @PrimaryKey val id : Int? = null
)
