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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StringField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isValid: (Boolean) -> Unit,
    onDone: () -> Unit,
    header: String,
    placeholder: String,
    maxLength: Int = 30
) {
    var textValue by remember { mutableStateOf(value) }
    var isNotInLimit by remember { mutableStateOf(false) }
    var isEmpty by remember { mutableStateOf(false) }

    Column (modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )

        Box (
            modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    isNotInLimit = it.length >= maxLength
                    isEmpty = it.isEmpty()
                    if(!isNotInLimit && !isEmpty) {
                        onValueChange(it)
                        isValid(true)
                    } else {
                        isValid(false)
                    }
                },
                decorationBox = { innerTextField ->
                    if (textValue.isEmpty()) {
                        Text(placeholder, color = Color.Gray, fontSize = 13.sp)
                        isValid(false)
                    }
                    innerTextField()
                },
                modifier = fieldModifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions( onDone = { if(!isNotInLimit && !isEmpty) onDone() }),
            )
            if(maxLength!=99){
                Text(
                    text = "${value.length}/$maxLength",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.White)
                )
            }
        }
        when {
            isNotInLimit -> Text(
                text = "Input is too long",
                color = Color.Red, fontSize = 10.sp
            )
            isEmpty -> Text(
                text = "Input cannot be empty",
                color = Color.Red, fontSize = 10.sp
            )
            else -> Text(
                text = "",
                color = Color.Red, fontSize = 10.sp
            )
        }
    }
}

