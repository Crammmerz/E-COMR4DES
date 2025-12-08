package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun DescriptionField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String) -> Unit,
    maxLength: Int = 350
) {
    val safeDescription = value ?: ""
    Column (modifier){
        Text(
            text = "Description",
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(10.dp)
        ) {
            BasicTextField(
                value = safeDescription,
                onValueChange = {
                    if (it.length <= maxLength) onValueChange(it)
                },
                modifier = fieldModifier.fillMaxSize(),
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                maxLines = 12,
                decorationBox = { innerTextField ->
                    if (safeDescription.isEmpty()) {
                        Text("Enter description...", color = Color.Gray, fontSize = 15.sp)
                    }
                    Box(Modifier.fillMaxSize()) { innerTextField() }
                }
            )
            Text(
                text = "${safeDescription.length}/$maxLength",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, top = 2.dp)
                    .background(Color.White)
            )
        }
    }
}
