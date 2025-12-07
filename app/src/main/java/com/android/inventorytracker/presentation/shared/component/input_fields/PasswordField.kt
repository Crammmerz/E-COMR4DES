package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    isValid: (Boolean) -> Unit,
    onDone: () -> Unit,
    header: String,
    minLength: Int = 3,
) {
    var textValue by remember { mutableStateOf(value) }
    var isValidLength by remember { mutableStateOf(false) }
    var isValidText by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    isValidLength = it.length <= minLength
                    isValidText = it != it.trim()
                    if (!isValidLength && !isValidText) {
                        onValueChange(it)
                        isValid(true)
                    } else {
                        isValid(false)
                    }
                },
                decorationBox = { innerTextField ->
                    if (textValue.isEmpty()) {
                        Text("Enter password", color = Color.Gray, fontSize = 13.sp)
                        isValid(false)
                    }
                    innerTextField()
                },
                maxLines = 1,
                singleLine = true,
                modifier = fieldModifier.weight(1f),
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions( onDone = { if(!isValidLength && !isValidText) onDone() }),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        }

        when {
            isValidLength -> Text(
                text = "Password must be at least $minLength characters long",
                color = Color.Red, fontSize = 10.sp
            )
            isValidText -> Text(
                text = "Password cannot start or end with a space",
                color = Color.Red, fontSize = 10.sp
            )
            else -> Text(
                text = "",
                color = Color.Red, fontSize = 10.sp
            )
        }
    }
}
