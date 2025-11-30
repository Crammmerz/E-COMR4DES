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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StringField(
    text: String,
    onTextChange: (String) -> Unit,
    header: String,
    modifier: Modifier = Modifier,
    maxLength: Int = 30
) {
    var textValue by rememberSaveable { mutableStateOf(text) }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
            .height(30.dp)
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
                onValueChange = { textValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus(true)
                        if(textValue.length <= maxLength && textValue.isNotEmpty()){
                            onTextChange(textValue)
                            isError = false
                        } else {
                            onTextChange(text)
                            isError = true
                        }
                    }
                )
            )
            Text(
                text = "${text.length}/$maxLength",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(Color.White)
            )
        }
        Text(text = if(isError) "Invalid Input" else "", color = Color.Red, fontSize = 10.sp)
    }
}

