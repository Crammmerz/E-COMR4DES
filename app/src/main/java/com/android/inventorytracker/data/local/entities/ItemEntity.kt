package com.android.inventorytracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String?,
    val name: String,
    val unitThreshold: Int,
    val subUnitThreshold: Int,
    val description: String?
)