package com.android.inventorytracker.presentation.popup.batch_insertion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.inventorytracker.data.model.ItemModel
import java.text.SimpleDateFormat
import java.util.*

// Colors accurately sampled from design references
val PopupBg = Color(0xFFFCF5EF)           //
val DarkBrownText = Color(0xFF513D36)     //
val ActionBrown = Color(0xFF513D36)       //
val CancelRedBrown = Color(0xFF946E64)    //
val ErrorRed = Color(0xFFE53935)          //

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchInsertionPopup(
    itemModel: ItemModel,
    onDismiss: () -> Unit
) {
    var unitValue by remember { mutableStateOf("0") }
    var subUnitValue by remember { mutableStateOf("0") }

    // Date State logic
    var expiryDateText by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Format the date when selected
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        expiryDateText = formatter.format(Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK", color = ActionBrown) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(330.dp) //
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp), //
            color = PopupBg
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 28.dp, vertical = 24.dp) //
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add Stock", //
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = DarkBrownText
                )

                Text(
                    text = "Specify the expiry date and amount you wish to add.", //
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 22.dp)
                )

                // 1. Expiry Date Field (Functional Calendar)
                StyledInputField(
                    label = "Expiry Date",
                    value = expiryDateText,
                    onValueChange = { expiryDateText = it },
                    placeholder = "MM/DD/YYYY",
                    isDatePicker = true,
                    readOnly = true, // Prevents keyboard, forces calendar click
                    onIconClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(14.dp)) //

                // 2. Unit Field
                StyledInputField(
                    label = "Unit",
                    value = unitValue,
                    onValueChange = { unitValue = it },
                    isError = unitValue.isEmpty() //
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Sub Unit Field
                StyledInputField(
                    label = "Sub Unit",
                    value = subUnitValue,
                    onValueChange = { subUnitValue = it },
                    isError = subUnitValue.isEmpty() //
                )

                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CancelRedBrown), //
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CancelRedBrown)
                    ) {
                        Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActionBrown) //
                    ) {
                        Text("Add Stock", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isDatePicker: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onIconClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = DarkBrownText,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = { Text(placeholder, color = Color.LightGray, fontSize = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isDatePicker) Modifier.clickable { onIconClick?.invoke() }
                    else Modifier
                ),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.7f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White
            ),
            trailingIcon = if (isDatePicker) {
                {
                    IconButton(onClick = { onIconClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange, //
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            isError = isError,
            singleLine = true
        )

        if (isError) {
            Text(
                text = "Invalid Input", //
                color = ErrorRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}