package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R

@Composable
fun IntroScreen(){
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Welcome to Inventory Tracker",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = rememberAsyncImagePainter(
                model = R.mipmap.ic_launcher,
                placeholder = painterResource(R.drawable.baseline_image_24)
            ),
            contentDescription = "Selected image",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Crop,
        )
    }
}