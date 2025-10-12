package com.android.inventorytracking.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalQuantity: Double,
    val minStock: Double,
    val maxStock: Double,
    val imageUri: String?,
    val description: String?
)


