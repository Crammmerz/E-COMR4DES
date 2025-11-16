package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collectLatest

@OptIn(FlowPreview::class)
@Composable
fun SearchBar(
    itemViewModel: ItemViewModel,
    modifier: Modifier = Modifier,
    maxLength: Int = 30
) {
    var name by rememberSaveable { mutableStateOf("") }

    // 🔹 Debounce search input: fires only after user stops typing for 300ms
    LaunchedEffect(Unit) {
        snapshotFlow { name }
            .debounce(300) // wait 300ms after typing stops
            .filter { it.length <= maxLength }
            .distinctUntilChanged()
            .collectLatest { query ->
                itemViewModel.setSearchQuery(query.trim())
            }
    }

    Row(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading search icon
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Text field with placeholder
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = name,
                onValueChange = { name = it.take(maxLength) },
                textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { itemViewModel.setSearchQuery(name.trim()) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (name.isEmpty()) {
                Text(
                    text = "Search Items...",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }

        // Trailing clear button
        if (name.isNotEmpty()) {
            IconButton(
                onClick = {
                    name = ""
                    itemViewModel.setSearchQuery("")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color.Gray
                )
            }
        }
    }
}