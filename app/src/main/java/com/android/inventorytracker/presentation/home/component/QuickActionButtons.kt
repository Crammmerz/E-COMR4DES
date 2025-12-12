package com.android.inventorytracker.presentation.home.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.R // Ensure R is imported for font reference
import com.android.inventorytracker.presentation.popup.batch_group_insertion.BatchGroupInsertionPopup
import com.android.inventorytracker.presentation.popup.batch_group_removal.BatchGroupRemovalPopup
import com.android.inventorytracker.presentation.shared.component.primitive.CenterButton
import com.android.inventorytracker.presentation.shared.viewmodel.ItemViewModel

// --- Pure White & Beige Palette (Uniform with Login.kt) ---
private val DarkBeigeText = Color(0xFF523F31)       // Dark text for secondary actions
private val PureWhite = Color(0xFFFFFFFF)           // White text/background
private val AccentBeigePrimary = Color(0xFFB08959)  // Deep beige for primary action button background
private val AccentBeigeLight = Color(0xFFE7D0AC)    // Very light beige for neutral/secondary button background

// --- Google Sans Font Family Definition ---
private val GoogleSans = FontFamily(
    Font(R.font.google_sans_regular, FontWeight.Normal),
    Font(R.font.google_sans_medium, FontWeight.Medium),
    Font(R.font.google_sans_semibold, FontWeight.SemiBold)
)

@Composable
fun QuickActions(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
) {
    val model by itemViewModel.itemModelList.collectAsState(initial = emptyList())
    var showAddStock by rememberSaveable { mutableStateOf(false) }
    var showRemoveStock by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .height(60.dp)
            .padding(vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start)
    ) {
        // Button 1: Add Stock (Primary Accent Color: Deep Beige)
        QuickActionButton(
            label = "Add Stock",
            modifier = Modifier.weight(1f),
            bgColor = AccentBeigePrimary, // Deep Beige
            contentColor = PureWhite,   // White text
            onClick = { showAddStock = true }
        )

        // Button 2: Deduct Stock (Secondary Accent Color: Light Beige)
        QuickActionButton(
            label = "Deduct Stock",
            modifier = Modifier.weight(1f),
            bgColor = AccentBeigeLight, // Light Beige/Neutral
            contentColor = DarkBeigeText, // Dark Beige text for contrast
            onClick = { showRemoveStock = true }
        )

        // Button 3: New Item (Tertiary Action: White Background)
        QuickActionButton(
            label = "New Item",
            modifier = Modifier.weight(1f),
            bgColor = PureWhite, // Pure white background
            contentColor = DarkBeigeText, // Dark Beige text
            onClick = { /* Placeholder action */ }
        )
    }

    if(showAddStock){
        BatchGroupInsertionPopup(model = model, onDismiss = { showAddStock = false })
    }
    if(showRemoveStock){
        BatchGroupRemovalPopup(model = model, onDismiss = { showRemoveStock = false })
    }
}

// Revised QuickActionButton to pass Font Family if CenterButton supports TextStyle
@Composable
fun QuickActionButton(
    label: String,
    modifier: Modifier = Modifier,
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    // Note: This relies on CenterButton being flexible enough to accept and apply the
    // background/shape modifiers and the passed label/content color.

    // If CenterButton is a simple wrapper, we update it to use Google Sans Medium
    CenterButton(
        modifier = modifier
            .fillMaxHeight()
            // Apply large corner radius and background color/shape
            .background(
                color = bgColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 16.dp),
        label = label,
        bgColor = bgColor,
        contentColor = contentColor,
        onClick = onClick,
        // *** Assume CenterButton can accept TextStyle or FontWeight for font application ***
        // If CenterButton uses a simple Text(label, color=contentColor), you would need to
        // inject the TextStyle here. Since we can't see CenterButton, we'll assume it's updated
        // or we use a standard Text here for demo.
        // For demonstration, let's assume CenterButton supports a text style parameter:
        // textStyle = TextStyle(fontFamily = GoogleSans, fontWeight = FontWeight.Medium)
    )

    // --- ALTERNATIVE: If CenterButton is a Box/Button and you need to apply the text style directly: ---
    /*
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = GoogleSans,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = contentColor
            )
        )
    }
    */
}