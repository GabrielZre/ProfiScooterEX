package com.example.profiscooterex.di

import com.example.profiscooterex.location.LocationChecker
import com.example.profiscooterex.location.impl.AndroidLocationChecker
import com.example.profiscooterex.permissions.PermissionChecker
import com.example.profiscooterex.permissions.impl.AndroidPermissionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityRetainedModule {

    @ActivityRetainedScoped
    @Provides
    fun providePermissionChecker(impl: AndroidPermissionChecker) : PermissionChecker {
        return impl
    }

    @ActivityRetainedScoped
    @Provides
    fun provideLocationChecker(impl: AndroidLocationChecker) : LocationChecker {
        return impl
    }
}