package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.entities.UserEntity

interface UserRepository {

    suspend fun authenticate(user: UserEntity): Boolean

    suspend fun register(username: String, rawPassword: String, role: String = "STAFF")

    suspend fun updateUser(user: UserEntity, newPass: String): Boolean

    suspend fun updateUserStaff(user: UserEntity): Boolean
}
