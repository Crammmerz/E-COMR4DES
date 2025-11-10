package com.android.inventorytracker.presentation.shared.component.item_property_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnitField(
    unit: Int,
    onUnitChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by rememberSaveable { mutableStateOf(unit.toString()) }

    LaunchedEffect(unit) {
        if (unit.toString() != textValue) {
            textValue = unit.toString()
        }
    }
    Column(modifier) {
        Text(
            text = "Threshold",
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
                onValueChange = {
                    textValue = it
                    val parsed = it.toIntOrNull()
                    if (parsed != null && parsed > 0) {
                        onUnitChange(parsed)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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


@Composable
fun UnitFieldFloat(
    unit: Float,
    onUnitChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by rememberSaveable { mutableStateOf(unit.toString()) }

    LaunchedEffect(unit) {
        if (unit.toString() != textValue) {
            textValue = unit.toString()
        }
    }
    Column(modifier) {
        Text(
            text = "Unit",
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
                onValueChange = {
                    textValue = it
                    val parsed = it.toFloatOrNull()
                    if (parsed != null && parsed > 0) {
                        onUnitChange(parsed)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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

