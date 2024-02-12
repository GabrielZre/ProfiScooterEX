package com.example.profiscooterex.di

import android.content.Context
import com.example.profiscooterex.permissions.PermissionChecker
import com.example.profiscooterex.permissions.bluetooth.BluetoothChecker
import com.example.profiscooterex.permissions.bluetooth.impl.AndroidBluetoothChecker
import com.example.profiscooterex.permissions.impl.AndroidPermissionChecker
import com.example.profiscooterex.permissions.location.LocationChecker
import com.example.profiscooterex.permissions.location.impl.AndroidLocationChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {

    @ActivityRetainedScoped
    @Provides
    fun providePermissionChecker(impl: AndroidPermissionChecker): PermissionChecker {
        return impl
    }

    @ActivityRetainedScoped
    @Provides
    fun provideLocationChecker(impl: AndroidLocationChecker): LocationChecker {
        return impl
    }

    @ActivityRetainedScoped
    @Provides
    fun provideBluetoothChecker(impl: AndroidBluetoothChecker): BluetoothChecker {
        return impl
    }

    @ActivityRetainedScoped
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
