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
fun PasswordField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    onDone: (() -> Unit)? = null,
    header: String,
    minLength: Int = 3,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val isLengthError = value.isNotEmpty() && value.length < minLength
    val isFormatError = value != value.trim()

    val isValid = value.isNotEmpty() && !isLengthError && !isFormatError

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
                    color = if (isLengthError || isFormatError) Color.Red else Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = fieldModifier
                    .weight(1f)
                    .fillMaxHeight(),
                textStyle = TextStyle(fontSize = 15.sp),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (onDone != null) {
                            onDone()
                        } else {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text("Enter password", color = Color.Gray, fontSize = 13.sp)
                        }
                        innerTextField()
                    }
                }
            )
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.size(30.dp)
            ) {
                val image =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                Icon(
                    imageVector = image,
                    contentDescription = description,
                    tint = Color.Gray
                )
            }
        }

        if (isLengthError) {
            Text(
                text = "Password must be at least $minLength characters long",
                color = Color.Red,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        } else if (isFormatError) {
            Text(
                text = "Password cannot start or end with a space",
                color = Color.Red,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}