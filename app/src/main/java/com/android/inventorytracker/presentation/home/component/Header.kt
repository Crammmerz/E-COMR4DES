package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.inventorytracker.R // Ensure R is imported for font reference
import com.android.inventorytracker.presentation.shared.viewmodel.TimeViewModel

// --- Pure White & Beige Palette (Uniform with Login.kt) ---
private val DarkBeigeText = Color(0xFF523F31)    // Dark text (Primary contrast)
private val LightBeigeText = Color(0xFF796254)   // Lighter text (Subtle details)

// --- Google Sans Font Family Definition ---
private val GoogleSans = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun Header(){
    val timeViewModel: TimeViewModel = viewModel()

    // Use a Column to stack the elements vertically
    Column(modifier = Modifier.fillMaxWidth()) {

        // 1. Date/Time (Subtle, Light Beige Color, Regular Font Weight)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = timeViewModel.getDayAndDate(),
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Normal, // Regular
                    fontSize = 16.sp,
                    color = LightBeigeText // Use light beige for subtlety
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Small separator

        // 2. Primary Title ("Inventory Dashboard" - Large, SemiBold)
        Text(
            text = "Inventory Dashboard",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold, // SemiBold for prominence
                fontSize = 34.sp,
                color = DarkBeigeText // Dark beige for prominence
            )
        )

        Spacer(modifier = Modifier.height(16.dp)) // Spacer before the actual Quick Actions component starts

        // 3. Secondary Title ("Quick Actions" - Medium size, Medium Weight)
        Text(
            text = "Quick Actions",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium, // Medium weight for section header
                fontSize = 20.sp,
                color = DarkBeigeText // Dark beige for contrast
            )
        )
    }
}