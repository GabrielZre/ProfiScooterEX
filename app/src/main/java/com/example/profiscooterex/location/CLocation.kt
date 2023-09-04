package com.example.profiscooterex.location

import android.location.Location

class CLocation(location: Location) : Location(location) {

    companion object {
        private const val feet = 3.28083989501312f
    }

    override fun distanceTo(dest: Location): Float {
        val nDistance = super.distanceTo(dest)
        return nDistance
    }

    override fun getAltitude(): Double {
        val nAltitude = super.getAltitude()
        return nAltitude
    }

    override fun getSpeed(): Float {
        val nSpeed = super.getSpeed() * 3.6f
        return nSpeed
    }

    override fun getAccuracy(): Float {
        val nAccuracy = super.getAccuracy()
        return nAccuracy
    }

    override fun getLatitude(): Double {
        return super.getLatitude()
    }

    override fun getLongitude(): Double {
        return super.getLongitude()
    }
}