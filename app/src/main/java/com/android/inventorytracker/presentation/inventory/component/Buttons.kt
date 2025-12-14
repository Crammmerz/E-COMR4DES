package com.android.inventorytracker.presentation.inventory.component



import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp



@Composable

fun AddNewItemButton(onClick: () -> Unit) {

    Button(

        onClick = onClick //TODO: Design The Button

    ) {

        Text("Add new Item")

    }

}



@Composable

fun DeleteItemButton(onClick: () -> Unit, enabled: Boolean){

    Button(

        onClick = onClick, //TODO: Design The Button

        enabled = enabled

    ) {

        Text("Remove Item")

    }

}



@Composable

fun ItemButton(text: String, enabled: Boolean = true, modifier: Modifier, onClick: () -> Unit) {

    Button(

        modifier = modifier,

        enabled = enabled,

        onClick = onClick, //TODO: Design The Button

    ) {

        Text(text, fontSize = 12.sp,)

    }

}