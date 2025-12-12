package com.android.inventorytracker.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.R // Ensure R is imported for font reference
import com.android.inventorytracker.data.model.ItemModel

// --- Pure White & Beige Palette (Uniform with Login.kt) ---
private val PureWhite = Color(0xFFFFFFFF)        // Card background
private val DarkBeigeText = Color(0xFF523F31)    // Dark text (Primary contrast)
private val LightBeigeText = Color(0xFF796254)   // Lighter text (Subtle details)
private val BeigeProgressTrack = Color(0xFFEEEAE5) // Very light beige/off-white for progress bar track

// --- Google Sans Font Family Definition ---
private val GoogleSans = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun StockLevels(modifier: Modifier, itemModel: List<ItemModel>){

    Column(modifier = modifier
        .background(PureWhite)
        .padding(24.dp)
    ){
        // Header Text
        Text(
            text = "Stocks",
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.SemiBold, // SemiBold for header
                fontSize = 24.sp,
                color = DarkBeigeText
            )
        )

        // List of Stock Items
        LazyColumn (
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(itemModel) { model ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Item Name
                    Text(
                        model.item.name,
                        style = TextStyle(
                            fontFamily = GoogleSans,
                            fontWeight = FontWeight.Medium, // Medium for item name
                            fontSize = 16.sp,
                            color = DarkBeigeText
                        )
                    )

                    // Progress Bar Row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Linear Progress Indicator (iOS Look)
                        LinearProgressIndicator(
                            progress = { (model.totalUnit().toFloat() / model.item.unitThreshold.toFloat()) },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .height(8.dp),
                            color = model.stockColor, // Retain model's color for visual alert
                            trackColor = BeigeProgressTrack // Use light beige track color
                        )

                        // Count Text
                        Text(
                            text = "${model.totalUnitFormatted()} / ${model.item.unitThreshold}",
                            modifier = Modifier.width(100.dp),
                            textAlign = TextAlign.End,
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontWeight = FontWeight.Normal, // Regular for detail count
                                fontSize = 14.sp,
                                color = LightBeigeText // Use light beige for subtle count text
                            )
                        )
                    }
                }
            }
        }
    }
}