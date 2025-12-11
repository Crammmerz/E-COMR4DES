package com.android.inventorytracker.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.home.Home
import com.android.inventorytracker.presentation.inventory.Inventory
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.presentation.main.viewmodel.Content
import com.android.inventorytracker.presentation.main.viewmodel.MainViewModel
import com.android.inventorytracker.presentation.settings.Setting
import com.android.inventorytracker.ui.theme.Sand
import com.android.inventorytracker.util.toLocalDate

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    val currentContent by mainViewModel.currentContent.collectAsState()
    val showNavBar by mainViewModel.showNavBar.collectAsState()


    Surface (
        color = Sand,
        modifier = modifier
            .then(
            if (showNavBar) {
                Modifier.clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )  {
                    mainViewModel.setNavBar(false)
                }
            } else {
                Modifier
            }
        )
    ) {
        when (currentContent) {
            Content.Home -> Home()
            Content.Inventory -> Inventory()
            Content.Setting -> Setting()
        }
    }
}