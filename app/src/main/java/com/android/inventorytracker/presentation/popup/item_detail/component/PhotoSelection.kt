package com.android.inventorytracker.presentation.popup.item_detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.inventorytracker.R

@Composable
fun PhotoSelection(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.outline_add_photo_alternate_24),
        contentDescription = "Placeholder image",
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.85f)
            .clip(RoundedCornerShape(5.dp)),
        contentScale = ContentScale.Crop
    )
}