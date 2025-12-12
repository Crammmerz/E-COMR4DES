package com.android.inventorytracker.data.repository

import android.util.Log
import com.android.inventorytracker.data.local.dao.UserDao
import com.android.inventorytracker.data.local.entities.UserEntity
import com.android.inventorytracker.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
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

    override suspend fun updateUser(user: UserEntity, newPass: String,): Boolean {
        if (!authenticate(user)) return false

        val updatedUser = user.copy(passwordHash = hashPassword(newPass))
        return try {
            userDao.update(updatedUser)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateUserStaff(user: UserEntity): Boolean {
        return try {
            userDao.updatePassStaff(user.username, hashPassword(user.passwordHash), user.role)
            true
        } catch (e: Exception) {
            false
        }
    }
}