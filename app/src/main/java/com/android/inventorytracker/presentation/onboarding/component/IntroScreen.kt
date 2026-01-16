package com.android.inventorytracker.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.android.inventorytracker.R
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette

@Composable
fun IntroScreen(onGetStartedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // Allowance para sa button sa baba
    ) {
        // IMAGE & TEXT SECTION: Gitna vertically, pero usog pakanan (paddingStart)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 120.dp) // Inusog pakanan ang buong row
                .align(Alignment.CenterStart), // Nakagitna vertically
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.mipmap.ic_launcher,
                    placeholder = painterResource(R.drawable.baseline_image_24)
                ),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(36.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(44.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Palette.DarkBeigeText.copy(alpha = 0.6f),
                        letterSpacing = (-0.5).sp
                    )
                )

                Text(
                    text = "StockWise",
                    maxLines = 1,
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Palette.DarkBeigeText,
                        lineHeight = 76.sp,
                        letterSpacing = (-3).sp
                    )
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(end = 80.dp, bottom = 40.dp) // Layout padding sa dulo
                .width(220.dp)
                .height(60.dp)
                .align(Alignment.BottomEnd), // Fix sa lower right
            onClick = onGetStartedClick,
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Palette.ButtonBeigeBase,
                contentColor = Palette.ButtonDarkBrown
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Get Started",
                style = TextStyle(
                    fontFamily = GoogleSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
    }
}