package com.android.inventorytracker.presentation.shared.component.input_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onValidityChange: (Boolean) -> Unit,
    onDone: (() -> Unit)? = null,
    header: String,
    placeholder: String,
) {
    val isError = value.isNotEmpty() && !isValidDate(value)
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(value))
    }

    Column(modifier) {
        // Label
        Text(
            text = header,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else Color.DarkGray,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val rawDigits = newValue.text.filter { it.isDigit() }.take(8)
                    val formatted = formatDateInput(newValue.text)

                    onValueChange(formatted)
                    onValidityChange(isValidDate(formatted))

                    val addedChars = formatted.length - rawDigits.length
                    val newCursor = (newValue.selection.end + addedChars).coerceAtMost(formatted.length)

                    textFieldValue = TextFieldValue(
                        text = formatted,
                        selection = TextRange(newCursor)
                    )
                },
                modifier = fieldModifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (onDone != null) {
                            onDone()
                        } else {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
                ),
                decorationBox = { innerTextField ->
                    if (textFieldValue.text.isEmpty()) {
                        Text(placeholder, color = Color.Gray, fontSize = 13.sp)
                    }
                    innerTextField()
                }
            )

            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { showDatePicker = true }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Pick Date",
                    tint = Color.Gray
                )
            }
        }

        if (isError) {
            Text(
                text = "Invalid Date (MM/DD/YYYY)",
                color = MaterialTheme.colorScheme.error,
                fontSize = 10.sp
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            val formatted =
                                selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                            textFieldValue = TextFieldValue(formatted, TextRange(formatted.length))
                            onValueChange(formatted)
                            onValidityChange(true)
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Helper: Auto-slash formatting without blocking typing
private fun formatDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    return buildString {
        for (i in digits.indices) {
            append(digits[i])
            if ((i == 1 || i == 3) && i < digits.lastIndex) append('/')
        }
    }
}

// Helper: Strict validation
private fun isValidDate(date: String): Boolean {
    if (date.length != 10) return false
    return try {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        LocalDate.parse(date, formatter)
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

