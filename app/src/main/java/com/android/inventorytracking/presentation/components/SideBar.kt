package com.android.inventorytracking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SideBar(bgColor: Color,modifier: Modifier = Modifier) {
    Surface(
        color = bgColor,
        tonalElevation = 10.dp,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.15f)// fixed width for sidebar
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = 15.dp,
                        bottom = 15.dp
                        ),
                text = "📦 Lumi Cafe",
                color = Color.White
            )
            Home()
            Inventory()
        }
    }
}

@Composable
fun Home(){
    Button(
        onClick = { /* TODO: Handle inventory click */ },
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(0.99f)
            .padding(0.dp)
            .height(30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = " 🏠 Home")

            Spacer(modifier = Modifier.weight(1f)) // Pushes next item to the right
        }
    }
}
@Composable
fun Inventory(){
    Button(
        onClick = { /* TODO: Handle inventory click */ },
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(0.99f)
            .height(30.dp)
            .padding(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = " 🧾 Inventory")

            Spacer(modifier = Modifier.weight(1f)) // Pushes next item to the right

            Text(">") // Right-aligned
        }
    }
}



