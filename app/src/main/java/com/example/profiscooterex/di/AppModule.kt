package com.example.profiscooterex.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.profiscooterex.MainActivity
import com.example.profiscooterex.data.AuthRepository
import com.example.profiscooterex.data.AuthRepositoryImpl
import com.example.profiscooterex.data.ble.BatteryVoltageReceiveManager
import com.example.profiscooterex.data.ble.service.BatteryVoltageBLEReceiveManager
import com.example.profiscooterex.permissions.service.RequestServiceListener
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
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
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