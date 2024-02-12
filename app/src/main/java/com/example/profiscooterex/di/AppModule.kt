package com.example.profiscooterex.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import com.example.profiscooterex.data.AuthRepository
import com.example.profiscooterex.data.AuthRepositoryImpl
import com.example.profiscooterex.data.ble.BatteryVoltageReceiveManager
import com.example.profiscooterex.data.ble.service.BatteryVoltageBLEReceiveManager
import com.example.profiscooterex.permissions.service.RequestServiceListener
import com.example.profiscooterex.data.DataViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideDataViewModel(): DataViewModel {
        return DataViewModel()
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter
    }

    @Provides
    @Singleton
    fun provideRequestServiceListener(): RequestServiceListener {
        return RequestServiceListener()
    }

    @Provides
    @Singleton
    fun provideBatteryVoltageReceiveManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): BatteryVoltageReceiveManager {
        return BatteryVoltageBLEReceiveManager(bluetoothAdapter, context)
    }
}