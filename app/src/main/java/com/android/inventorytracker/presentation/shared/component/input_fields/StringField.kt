package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StringField(
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidationChange: ((Boolean) -> Unit)? = null,
    onDone: (() -> Unit)? = null,
    header: String,
    placeholder: String,
    maxLength: Int = 30,
    showCounter: Boolean = true
) {
    val isTooLong = value.length > maxLength
    val isEmpty = value.isBlank()
    val hasError = isTooLong || isEmpty

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(hasError) {
        onValidationChange?.invoke(!hasError)
    }

    Column(modifier = modifier) {
        // Header
        Text(
            text = header,
            style = MaterialTheme.typography.labelMedium, // Use Theme styles
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White, RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    // Visual feedback for error state
                    color = if (hasError) Color.Red else Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = inputModifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(end = if (showCounter) 35.dp else 0.dp), // Prevent text overlapping counter
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
            )

            if (showCounter) {
                Text(
                    text = "${value.length}/$maxLength",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTooLong) Color.Red else Color.DarkGray,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.White.copy(alpha = 0.8f)) // Slight scrim for readability
                )
            }
        }

        // Error Messages
        // 5. Layout: Reserve space or use specific padding to prevent layout jumping?
        // Currently, this will push content down when error appears.
        if (hasError) {
            Text(
                text = when {
                    isTooLong -> "Input is too long"
                    isEmpty -> "Input cannot be empty"
                    else -> ""
                },
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}