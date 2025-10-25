package com.android.inventorytracker.presentation.inventory.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddNewItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick //TODO: Design The Button
    ) {
        Text("Add new Item")
    }
}

//@Composable
//fun AddNewItemButton(viewModel: ItemViewModel) {
//    val context = LocalContext.current
//    Button(
//        onClick = {
//            viewModel.insertItem(
//                imageUri = "content://images/item1.jpg",
//                name = "Coffee Beans",
//                unitThreshold = 10,
//                subUnitThreshold = 5,
//                description = "Arabica blend"
//            )
//            Toast.makeText(context, "Item added!", Toast.LENGTH_SHORT).show()
//        }
//    ) {
//        Text("Add new Item")
//    }
//}
