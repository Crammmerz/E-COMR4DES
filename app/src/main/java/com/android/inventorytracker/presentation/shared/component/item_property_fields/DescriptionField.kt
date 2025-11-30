package com.android.inventorytracker.presentation.shared.component.item_property_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
    description: String?,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = 350
) {
    val safeDescription = description ?: ""
    Column (modifier){
        Text(
            text = "Description",
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
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
                    if (it.length <= maxLength) onDescriptionChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 15.sp),
                cursorBrush = SolidColor(Color.Black),
                maxLines = 12,
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
