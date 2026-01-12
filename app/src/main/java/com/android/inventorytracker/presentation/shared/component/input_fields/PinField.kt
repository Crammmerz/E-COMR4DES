package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    onDone: (() -> Unit)? = null,
    header: String = "Enter PIN",
    minLength: Int = 4, // PINs are usually 4 or 6 digits
) {
    var pinVisible by remember { mutableStateOf(false) }

    // Validation logic
    val isLengthError = value.isNotEmpty() && value.length < minLength
    val isValid = value.isNotEmpty() && value.length >= minLength

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isValid) {
        onValidityChange(isValid)
    }

    Column(modifier = modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White, RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    color = if (isLengthError) Color.Red else Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    // ONLY allow digits and limit length (optional, e.g., to 6)
                    if (newValue.all { it.isDigit() } && newValue.length <= 6) {
                        onValueChange(newValue)
                    }
                },
                modifier = fieldModifier
                    .weight(1f)
                    .fillMaxHeight(),
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions(
                    // Changed to NumberPassword for numeric pad
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (onDone != null) {
                            onDone()
                        } else {
                            focusManager.clearFocus(force = true)
                            keyboardController?.hide()
                        }
                    }
                ),
                visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text("0000", color = Color.Gray, fontSize = 13.sp)
                        }
                        innerTextField()
                    }
                }
            )
            IconButton(
                onClick = { pinVisible = !pinVisible },
                modifier = Modifier.size(30.dp)
            ) {
                val image = if (pinVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(
                    imageVector = image,
                    contentDescription = if (pinVisible) "Hide PIN" else "Show PIN",
                    tint = Color.Gray
                )
            }
        }

        if (isLengthError) {
            Text(
                text = "PIN must be at least $minLength digits",
                color = Color.Red,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}