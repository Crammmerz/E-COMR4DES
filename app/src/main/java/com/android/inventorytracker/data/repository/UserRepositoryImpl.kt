package com.android.inventorytracker.data.repository

import android.util.Log
import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override suspend fun authenticate(user: UserEntity): Boolean {
        val hashed = hashPassword(user.passwordHash)
        val user = userDao.getUserCredential(user.username, hashed, user.role)
        return user != null
    }

    override suspend fun register(username: String, rawPassword: String, role: String) {
        val hashed = hashPassword(rawPassword)
        userDao.insert(UserEntity(username = username, passwordHash = hashed, role = role))
    }

    override suspend fun updateUser(newPass: String, role: String): Boolean {
        val existingUser = userDao.getUserByRole(role)
        if (existingUser == null) return false

        val updatedUser = existingUser.copy(passwordHash = hashPassword(newPass))
        return try {
            userDao.update(updatedUser)
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to update user", e)
            false
        }
    }

    override fun getCount(role: String): Flow<Int> {
        return userDao.getCount(role)
    }
}