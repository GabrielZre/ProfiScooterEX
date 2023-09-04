package com.example.profiscooterex.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.profiscooterex.data.userDB.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationLiveData(var context: Context) : LiveData<LocationDetails>() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var lastFetchedLocation : Location? = null
    private var distanceTrip : Float = 0f

    private var omitFirstLocationRequest = true

    override fun onActive() {
        super.onActive()
        locationPermissionCheck()
        startLocationUpdates()
    }

    internal fun startLocationUpdates() {
        locationPermissionCheck()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setTripData(location: Location?) {
        location?.let {
                location ->
            value = LocationDetails(calculateDistanceTrip(location, lastFetchedLocation!!), speedToKmh(location.speed))
            lastFetchedLocation = location
        }
    }

    private fun calculateDistanceTrip(location: Location, previousLocation: Location) : Float {
        distanceTrip += location.distanceTo(previousLocation)
        return distanceTrip
    }

    private fun speedToKmh(currentSpeed: Float) : Float {
        return  currentSpeed * 3.6f
    }

    internal fun resetLocationData() {
        setNewLastFetchedLocation()
        distanceTrip = 0f
        value = LocationDetails(0f, 0f)
    }

    private fun setNewLastFetchedLocation() {
        lastFetchedLocation = null
        omitFirstLocationRequest = true
    }

    internal fun removeLocationUpdates() {
        setNewLastFetchedLocation()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return

            if (omitFirstLocationRequest) {
                Log.d("tag", "previousLoc null")
                omitFirstLocationRequest = false
            } else {
                if (lastFetchedLocation == null) {
                    lastFetchedLocation = locationResult.lastLocation
                }
                if ((lastFetchedLocation?.latitude != locationResult.lastLocation?.latitude) &&
                    lastFetchedLocation?.longitude != locationResult.lastLocation?.longitude
                ) {
                    setTripData(locationResult.lastLocation)
                }
            }
        }
    }


    private fun locationPermissionCheck() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    companion object {
        private const val FIVE_SECS: Long = 5000
        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, FIVE_SECS)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(FIVE_SECS)
            .setMaxUpdateDelayMillis(FIVE_SECS)
            .build()
    }
}