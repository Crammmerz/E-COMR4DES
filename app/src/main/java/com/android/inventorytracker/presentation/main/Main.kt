package com.android.inventorytracker.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.main.component.ContentSection
import com.android.inventorytracker.presentation.main.component.NavBar
import com.android.inventorytracker.presentation.main.component.TopBar
import com.android.inventorytracker.presentation.main.viewmodel.ContentViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel


@Composable
fun Main(
    itemViewModel: ItemViewModel = hiltViewModel(),
    batchViewModel: BatchViewModel = hiltViewModel(),
    contentViewModel: ContentViewModel = hiltViewModel()
) {
    Scaffold(topBar = { TopBar() }) { inner ->
        Row(modifier = Modifier
            .padding(inner)
            .fillMaxSize()
        ) {
            NavBar(
                modifier = Modifier.widthIn(min = 200.dp, max = 320.dp),
                viewModel = contentViewModel
            )
            ContentSection(
                modifier = Modifier.weight(1f),
                itemViewModel = itemViewModel,
                batchViewModel = batchViewModel,
                contentViewModel = contentViewModel
            )
        }
    }
}
