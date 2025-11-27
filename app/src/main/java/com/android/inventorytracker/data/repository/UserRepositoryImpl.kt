package com.android.inventorytracker.data.repository

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

    override suspend fun login(username: String, rawPassword: String, role: String): Boolean {
        val hashed = hashPassword(rawPassword)
        val user = userDao.getUserCredential(username, hashed, role)
        return user != null
    }

    override suspend fun register(username: String, rawPassword: String, role: String) {
        val hashed = hashPassword(rawPassword)
        userDao.insert(UserEntity(username = username, passwordHash = hashed, role = role))
    }
}