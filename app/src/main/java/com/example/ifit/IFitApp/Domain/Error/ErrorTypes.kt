package com.example.ifit.IFitApp.Domain.Error

sealed class ErrorTypes{
    class ExistingGoalNameException(): Exception()
    class FieldsEmptyException() : Exception()
    class UnKnownError() : Exception()
}
