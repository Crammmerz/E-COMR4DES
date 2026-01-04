package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun authenticate(user: UserEntity): Boolean

    suspend fun register(username: String, rawPassword: String, role: String)

    suspend fun updateUser(newPass: String, role: String): Boolean

    fun getCount(role: String): Flow<Int>
}
