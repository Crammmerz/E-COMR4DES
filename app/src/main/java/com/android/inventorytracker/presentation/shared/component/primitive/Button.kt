package com.android.inventorytracker.presentation.shared.component.primitive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//TODO: Button Design
@Composable
fun AddItemButton(onClick: () -> Unit){
    Button(onClick = onClick) {
        Text("Add Item")
    }
}
@Composable
fun DeleteItemButton(onClick: () -> Unit){
    Button(onClick = onClick) {
        Text("Delete Item")
    }
}
@Composable
fun CancelButton(onClick: () -> Unit){
    Button(onClick = onClick) {
        Text("Cancel Item")
    }
}
@Composable
fun LeftButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = Color.Gray,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults
            .buttonColors(contentColor = contentColor,
                containerColor = bgColor),
        modifier = modifier
            .fillMaxWidth(1f)
            .height(30.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(modifier = Modifier.padding(horizontal = 20.dp),text = label)

        }
    }
}

@Composable
fun RightButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = Color.Gray,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults
            .buttonColors(contentColor = contentColor,
                containerColor = bgColor),
        modifier = modifier
            .fillMaxWidth(0.99f)
            .padding(0.dp)
            .height(30.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(modifier = Modifier.padding(horizontal = 10.dp), text = label)
        }
    }
}

@Composable
fun CenterButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = Color.Gray,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .height(30.dp)
    ) {
        Text(text = label)
    }
}


