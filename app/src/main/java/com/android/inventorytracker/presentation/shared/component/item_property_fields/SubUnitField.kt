package com.android.inventorytracker.presentation.shared.component.item_property_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun SubUnitField(
    subUnit: Int,
    onSubUnitChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by rememberSaveable { mutableStateOf(subUnit.toString()) }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(subUnit, isFocused) {
        if (!isFocused) {
            textValue = subUnit.toString()
        }
    }

    Column(modifier) {
        Text(
            text = "Sub Unit",
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 5.dp, bottom = 3.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = { textValue = it }, // only update local state
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (isFocused) {
                            textValue = "" // clear when focused
                        } else {
                            val parsed = textValue.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                onSubUnitChange(parsed) // commit only on blur
                            }
                        }
                    },
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val parsed = textValue.toIntOrNull()
                        if (parsed != null && parsed > 0) {
                            onSubUnitChange(parsed)
                        }
                        focusManager.clearFocus() // close keyboard
                    }
                )
            )

            Text(
                text = "unit",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
        }
    }
}
