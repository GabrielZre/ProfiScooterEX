package com.example.profiscooterex.ui.scooter

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.location.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScooterSettingsViewModel
@Inject constructor(
    application: Application,
    private val dataViewModel: DataViewModel,
) : AndroidViewModel(application) {


    private val scooterData: Scooter
        get() = dataViewModel.scooterDataState.value

    var batteryAh by mutableStateOf(scooterData.batteryAh)
    var batteryVoltage by mutableStateOf(scooterData.batteryVoltage)
    var bottomCutOff by mutableStateOf(scooterData.bottomCutOff)
    var motorWatt by mutableStateOf(scooterData.motorWatt)
    var upperCutOff by mutableStateOf(scooterData.upperCutOff)
}