package com.horizon.doveinventory.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.horizon.doveinventory.feature_auth.data.data_source.UserDatabase
import com.horizon.doveinventory.feature_auth.data.repository.UserRepositoryImpl
import com.horizon.doveinventory.feature_auth.domain.repository.UserRepository
import com.horizon.doveinventory.feature_auth.domain.use_case.GetUserIDUseCase
import com.horizon.doveinventory.feature_auth.domain.use_case.RegisterUserUseCase
import com.horizon.doveinventory.feature_auth.domain.use_case.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Same provider pattern as ItemModule, singleton since only one instance is needed app-wide
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase, firebaseAuth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(db.userDao, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(repository: UserRepository): UserUseCases {
        return UserUseCases(
            registerUserUseCase = RegisterUserUseCase(repository),
            getUserByIDUseCase = GetUserIDUseCase(repository)
        )
    }
}