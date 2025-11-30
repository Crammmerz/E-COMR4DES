package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun IntField(
    num: Int,
    onNumChange: (Int) -> Unit,
    header: String,
    modifier: Modifier = Modifier
) {
    var textValue by rememberSaveable { mutableStateOf(num.toString()) }
    var isFocused by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    LaunchedEffect(num, isFocused) {
        if (!isFocused) {
            textValue = num.toString()
        }
    }

    Column(modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.White)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center // centers both vertically & horizontally
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = { textValue = it }, // only update local state
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (isFocused) {
                            textValue = "" // clear when focused
                        } else {
                            val parsed = textValue.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                onNumChange(parsed)
                                isError = false
                            } else {
                                isError = true
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
                        focusManager.clearFocus(true)
                        val parsed = textValue.toIntOrNull()
                        if (parsed != null && parsed > 0) {
                            onNumChange(parsed)
                        }
                    }
                )
            )
        }

        Text(text = if(isError)"Invalid Input" else "", color = Color.Red, fontSize = 10.sp)
    }
}
