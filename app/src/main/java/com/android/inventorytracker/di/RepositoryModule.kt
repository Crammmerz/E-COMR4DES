package com.android.inventorytracker.di

import com.android.inventorytracker.data.repository.ItemRepository
import com.android.inventorytracker.data.repository.ItemRepositoryImpl
import com.android.inventorytracker.data.repository.UserRepository
import com.android.inventorytracker.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindItemRepository(
        impl: ItemRepositoryImpl
    ): ItemRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

}