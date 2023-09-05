package com.example.profiscooterex.location.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.location.LocationManagerCompat
import com.example.profiscooterex.location.LocationChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidLocationChecker @Inject constructor(@ApplicationContext val context : Context) :
    LocationChecker {

        override val locationState = mutableStateOf(
            LocationManagerCompat.isLocationEnabled(
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            )
        )

    private val locationIntentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as
                        LocationManager
                locationState.value = LocationManagerCompat.isLocationEnabled(locationManager)
            }
        }
    }

    init {
        context.registerReceiver(locationReceiver, locationIntentFilter)
    }
}