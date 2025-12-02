package com.android.inventorytracker.data.repository

interface UserRepository {

    suspend fun login(username: String, rawPassword: String, role: String): Boolean

    suspend fun register(username: String, rawPassword: String, role: String = "STAFF")

    suspend fun changePassword(user: String, password: String, role: String)
}
