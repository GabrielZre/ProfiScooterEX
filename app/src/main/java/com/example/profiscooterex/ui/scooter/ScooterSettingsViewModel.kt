package com.example.profiscooterex.ui.scooter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.profiscooterex.data.DataViewModel
import com.example.profiscooterex.data.userDB.Scooter
import com.example.profiscooterex.permissions.network.ConnectivityObserver
import com.example.profiscooterex.permissions.network.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScooterSettingsViewModel
@Inject
constructor(
    application: Application,
    private val dataViewModel: DataViewModel,
) : AndroidViewModel(application) {

    var connectivityObserver: ConnectivityObserver =
        NetworkConnectivityObserver(application.applicationContext)
    val scooterData: Scooter
        get() = dataViewModel.scooterDataState.value

    fun calculateStartIndex(
        scooterValue: String,
        minValue: String,
        maxValue: String,
        step: Int
    ): Int {
        return if (scooterValue.isNotBlank()) {
            val index = (scooterValue.toInt() - minValue.toInt()) / step
            index - 1
        } else {
            0
        }
    }

    fun saveScooterSettings(scooter: Scooter) {
        dataViewModel.sendScooterData(scooter)
    }
}
