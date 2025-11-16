package com.android.inventorytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.inventorytracker.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getUsersByRole(role: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE username = :username AND passwordHash = :passwordHash AND role= :role")
    suspend fun getUserCredential(username: String, passwordHash: String, role: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getUsersOrderedByUsername(): Flow<List<UserEntity>>
}
