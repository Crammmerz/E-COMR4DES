package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun FeatureHighlights() {
    val features = listOf(
        Pair("Secure account authentication", "Protect your data with modern authentication."),
        Pair("Track inventory effortlessly", "Stay on top of stock in real time, anywhere."),
        Pair("Real-time stock alerts", "Never miss critical updates on your items."),
        Pair("CSV Data Import Ready", "Import your existing data directly from CSV."),
        Pair("Precision Unit Tracking", "Manage grams, ml, or pieces with accuracy.")
    )

    // iOS-style ultra light beige/cream shade
    val iOSLightBeige = Color(0xFFFBF9F5)
    val iOSBorderColor = Color(0xFFEBE6DE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 120.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // --- HEADER ---
        Text(
            text = "Inventory Tracker",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Palette.DarkBeigeText,
                letterSpacing = (-2.5).sp
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "KEY FEATURES",
            modifier = Modifier.padding(bottom = 16.dp),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Palette.DarkBeigeText.copy(alpha = 0.5f),
                letterSpacing = 1.5.sp
            )
        )

        // --- FEATURE CARDS ---
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(end = 40.dp)
        ) {
            items(features) { (title, description) ->
                Surface(
                    modifier = Modifier
                        .width(380.dp)
                        .height(200.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = iOSLightBeige, // Mas light na beige/cream
                    border = BorderStroke(1.dp, iOSBorderColor) // Subtle border for iOS depth
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            maxLines = 1,
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Palette.ButtonDarkBrown // Mas dark para sa contrast
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = description,
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                color = Palette.ButtonDarkBrown.copy(alpha = 0.6f) // Subtle contrast
                            )
                        )
                    }
                }
            }
        }
    }
}