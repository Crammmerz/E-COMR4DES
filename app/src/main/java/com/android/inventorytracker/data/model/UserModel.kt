package com.android.inventorytracker.data.model

data class UserModel(
    val username: String,
    val password: String,
    val fullName: String,
    val admin: Boolean
)
