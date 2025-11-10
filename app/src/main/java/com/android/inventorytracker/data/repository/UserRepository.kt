package com.android.inventorytracker.data.repository

import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.UserEntity
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun login(username: String, rawPassword: String, role: String): Boolean {
        val hashed = hashPassword(rawPassword)
        val user = userDao.getUserCredential(username, hashed, role)
        return user != null
    }

    suspend fun register(username: String, rawPassword: String, role: String = "STAFF") {
        val hashed = hashPassword(rawPassword)
        userDao.insert(UserEntity(username = username, passwordHash = hashed, role = role))
    }

}
