package com.example.profiscooterex.permissions.location

import androidx.compose.runtime.MutableState

interface LocationChecker {
    val locationState: MutableState<Boolean>
}