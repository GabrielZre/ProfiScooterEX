package com.example.profiscooterex.permissions.service

import android.util.Log
import javax.inject.Inject

class RequestServiceListener @Inject constructor(){
    private val serviceCallListener = mutableListOf<RequestServicesListener>()

    fun addListener (listener: RequestServicesListener) {
        serviceCallListener.add(listener)
    }
    fun removeListener (listener: RequestServicesListener) {
        serviceCallListener.remove(listener)
    }
    fun notifyListeners () {
        Log.d("tag", "NOTIFIED FROM RSL")
        serviceCallListener.forEach { it.requestBluetooth() }
    }
    init {
        Log.d("tag", "inited RSL")
    }
}