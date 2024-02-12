package com.example.profiscooterex.permissions.service

import javax.inject.Inject

class RequestServiceListener @Inject constructor() {
    private val serviceCallListener = mutableListOf<RequestServicesListener>()

    fun addListener(listener: RequestServicesListener) {
        serviceCallListener.add(listener)
    }
    fun removeListener(listener: RequestServicesListener) {
        serviceCallListener.remove(listener)
    }
    fun notifyLocationListeners() {
        serviceCallListener.forEach { it.requestLocation() }
    }
    fun notifyBluetoothListeners() {
        serviceCallListener.forEach { it.requestBluetooth() }
    }
}
