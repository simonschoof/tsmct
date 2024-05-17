package com.simonschoof.tsmct.domain


sealed class Result<T>

typealias UnitResult = Result<Unit>

data class Success<T>(val value: T) : Result<T>() {
    companion object {
        val UNIT = Success(Unit)
    }
}
data class Failure<T>(val exception: Exception) : Result<T>()