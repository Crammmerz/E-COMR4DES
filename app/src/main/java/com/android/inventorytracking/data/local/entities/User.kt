package com.android.inventorytracking.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val username: String,
    val password: String,
    val fullName: String
)
