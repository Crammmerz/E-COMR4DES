package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.inventorytracker.ui.theme.GoogleSans

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Padding horizontal ay 12.dp para tapat sa Card padding ng ItemDataRow
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        // Ginagamit ang spacedBy(15.dp) para mag-match sa gap ng data rows
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // 1. Img Header - Naka-set sa 48.dp para tapat sa Image size sa baba
        Text(
            text = "Image",
            modifier = Modifier.width(48.dp),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        )

        // 2. Item Name Header - Weight 3.0f
        Text(
            text = "Item Name",
            modifier = Modifier.weight(3.0f),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        )

        // 3. Expiry Header - Weight 1.2f
        Text(
            text = "Nearest Expiry",
            modifier = Modifier.weight(1.2f),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        )

        // 4. Stock Header - Weight 0.8f
        Text(
            text = "Stock",
            modifier = Modifier.weight(0.8f),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        )

        // 5. Actions Header - Weight 1.5f (Para sa buttons sa dulo)
        Text(
            text = "Actions",
            modifier = Modifier.weight(1.5f),
            style = TextStyle(
                fontFamily = GoogleSans,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        )
    }
}