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
// ADD THIS IMPORT TO RESOLVE 'MaterialTheme'
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//TODO: Button Design
@Composable
fun ConfirmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // The previous error "No parameter with name 'containerColor' found." is fixed here
    // by having this parameter defined.
    containerColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp), // Using rounded corners consistent with theme
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor, // Using the custom color
            contentColor = Color.White // Ensures white text for dark backgrounds
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(text)
    }
}
// --- STYLING THE CANCEL BUTTON ---
// To match the cozy/professional theme, let's style the Cancel Button as a secondary,
// transparent/outlined button, which is standard practice.
val SecondaryButtonTextColor = Color(0xFF4A3B32) // Dark Brown

@Composable
fun CancelButton(text: String = "Cancel",onClick: () -> Unit){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Transparent background
            contentColor = SecondaryButtonTextColor // Dark Brown text
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Flat look
    ) {
        Text(text)
    }
}

// LeftButton, RightButton, and CenterButton remain unchanged as they were not the focus.
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
    contentColor: Color = Color.White,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .height(30.dp),
    ) {
        Text(text = label)
    }
}