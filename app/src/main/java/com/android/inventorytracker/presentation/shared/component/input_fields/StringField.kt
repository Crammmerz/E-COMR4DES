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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    Column (modifier) {
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
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
                value = text,
                onValueChange = {
                    if (it.length <= maxLength) onTextChange(it.take(maxLength))
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart),
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                maxLines = 1,
                singleLine = true
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
    }
}

