package com.example.profiscooterex.di

import android.app.Application
import com.example.profiscooterex.ApplicationViewModel
import com.example.profiscooterex.data.AuthRepository
import com.example.profiscooterex.data.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    /*@InstallIn(ViewModelComponent::class)
    @Module
    object ApplicationViewModelModule {
        @Provides
        @ViewModelScoped
        fun provideApplicationViewModel(
            @ApplicationContext app: Application
        ): ApplicationViewModel {
            return ApplicationViewModel(app)
        }
    }*/
}