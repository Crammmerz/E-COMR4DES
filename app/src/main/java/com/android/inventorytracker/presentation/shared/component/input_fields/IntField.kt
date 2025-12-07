package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction

@Composable
fun IntField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: IntRange = 1..9999,
    isValid: (Boolean) -> Unit,
    onDone: () -> Unit = {},
    header: String,
    placeholder: String,
    doClear: Boolean = false
) {
    var textValue by remember { mutableStateOf(value.toString()) }
    var isError by remember { mutableStateOf(false) }

    Column(modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(Color.White)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    val parsed = it.toIntOrNull()
                    val valid = parsed != null && parsed in valueRange

                    if (valid) {
                        isError = false
                        isValid(true)      // notify parent it's valid
                        onValueChange(parsed)
                    } else {
                        isError = true
                        isValid(false)     // notify parent it's invalid

                    }
                },
                decorationBox = { innerTextField ->
                    if (textValue.isEmpty()) {
                        Text(placeholder, color = Color.Gray, fontSize = 13.sp)
                    }
                    innerTextField()
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = fieldModifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .onFocusChanged { focusState ->
                        val isFocused = focusState.isFocused
                        if (isFocused && doClear) {
                            textValue = ""
                            isValid(false)
                        }
                    },
                keyboardActions = KeyboardActions( onDone = { onDone() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
        Text(
            text = if (isError) "Invalid Input" else "",
            color = Color.Red,
            fontSize = 10.sp
        )
    }
}
