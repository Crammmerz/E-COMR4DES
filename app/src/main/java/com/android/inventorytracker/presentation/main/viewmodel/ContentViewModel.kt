package com.android.inventorytracker.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class Content {
    Home, Inventory //etc
}

class ContentViewModel @Inject constructor(): ViewModel() {
    private val _currentContent = MutableStateFlow(Content.Home)
    val currentContent: StateFlow<Content> = _currentContent
    fun setContent(c: Content) { _currentContent.value = c }
}
