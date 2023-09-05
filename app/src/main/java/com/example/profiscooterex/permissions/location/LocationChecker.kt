package com.example.profiscooterex.location

import androidx.compose.runtime.MutableState

interface LocationChecker {
    val locationState: MutableState<Boolean>
}