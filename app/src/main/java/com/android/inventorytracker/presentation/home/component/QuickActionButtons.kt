package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.popup.batch_group_insertion.BatchGroupInsertionPopup
import com.android.inventorytracker.presentation.popup.batch_group_removal.BatchGroupRemovalPopup
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import com.android.inventorytracker.ui.theme.Palette
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.shadow

@Composable
fun QuickActions(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    val model by itemViewModel.itemModelList.collectAsState(initial = emptyList())

    var showAddStock by rememberSaveable { mutableStateOf(false) }
    var showRemoveStock by rememberSaveable { mutableStateOf(false) }
    var showRemoveCSV by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .height(60.dp)
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start)
    ) {
        QuickActionButton(
            label = "Add Stock",
            icon = Icons.Rounded.Add,
            modifier = Modifier.weight(1f),
            bgColor = Color(0xFF9D8A7C),
            contentColor = Color(0xFFFFFFFF),
            onClick = { showAddStock = true }
        )

        QuickActionButton(
            label = "Deduct Stock",
            icon = Icons.Rounded.Remove,
            modifier = Modifier.weight(1f),
            bgColor = Color(0xFF565449),
            contentColor = Color(0xFFFFFFFF),
            onClick = { showRemoveStock = true }
        )

        QuickActionButton(
            label = "Deduct Stocks (.csv)",
            icon = null, // No icon for CSV
            modifier = Modifier.weight(1f),
            bgColor = Color(0xFFE0E0E0),
            contentColor = Color(0xFF000000),
            onClick = { showRemoveCSV = true }
        )
    }

    if (showAddStock) {
        BatchGroupInsertionPopup(model = model, onDismiss = { showAddStock = false })
    }

    if (showRemoveStock) {
        BatchGroupRemovalPopup(model = model, onDismiss = { showRemoveStock = false })
    }

    if (showRemoveCSV) {
        ImportCsv(onDismiss = { showRemoveCSV = false })
    }
}

@Composable
fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    modifier: Modifier = Modifier,
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(999.dp),
                clip = false
            )
            .background(
                color = bgColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = label,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
