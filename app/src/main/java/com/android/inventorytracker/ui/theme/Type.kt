package com.android.inventorytracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // --- Headline Styles ---
    headlineLarge = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),

    // --- Title Styles (Ginagamit sa Popups at Headers) ---
    titleLarge = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    // --- Body Styles (Ginagamit sa main contents) ---
    bodyLarge = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    // --- Label Styles (ITO ANG IMPORTANTE PARA SA TEXT FIELDS AT BUTTONS) ---
    labelLarge = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp // Karaniwang ginagamit ng Buttons
    ),
    labelMedium = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp // Karaniwang ginagamit ng TextField Labels/Headers
    ),
    labelSmall = TextStyle(
        fontFamily = GoogleSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)