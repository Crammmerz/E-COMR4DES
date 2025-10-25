package com.android.inventorytracker.presentation.main.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel

enum class Content {
    Home, Inventory //etc
}

class ContentViewModel: ViewModel() {
    var currentContent by mutableStateOf(Content.Inventory)
}