package com.android.inventorytracker.presentation.popup.batch_insertion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.local.entities.ItemBatchEntity
import com.android.inventorytracker.data.model.ItemModel
import com.android.inventorytracker.presentation.shared.component.input_fields.DateField
import com.android.inventorytracker.presentation.shared.component.input_fields.FloatField
import com.android.inventorytracker.presentation.shared.component.input_fields.IntField
import com.android.inventorytracker.presentation.shared.component.primitive.DialogHost
import com.android.inventorytracker.presentation.shared.viewmodel.BatchViewModel
import com.android.inventorytracker.util.onSubUnitChange
import com.android.inventorytracker.util.onUnitChange
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester


private val DialogBg = Color(0xFFFFF8F3)
private val ActionBrown = Color(0xFF5D4037)

@Composable
fun BatchInsertionPopup(
    itemModel: ItemModel,
    batchViewModel: BatchViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    var unit by rememberSaveable { mutableFloatStateOf(0f) }
    var subUnit by rememberSaveable { mutableIntStateOf(0) }
    var dateValue by rememberSaveable { mutableStateOf("") }

    var validUnit by rememberSaveable { mutableStateOf(false) }
    var validDate by rememberSaveable { mutableStateOf(false) }
    val valid = validDate && validUnit

    val context = LocalContext.current

    val focusUnit = remember { FocusRequester() }
    val focusSubUnit = remember { FocusRequester() }
    val focusExpiry = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusExpiry.requestFocus()
    }

    fun doSubmit() {
        if(!validDate) Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
        if(!validUnit) Toast.makeText(context, "Please enter valid unit/subunit", Toast.LENGTH_SHORT).show()
        if (valid) {
            val selectedDate = runCatching {
                LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }.getOrNull()

            if (selectedDate != null) {
                val batch = ItemBatchEntity(
                    itemId = itemModel.item.id,
                    subUnit = subUnit,
                    expiryDate = selectedDate
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                )
                batchViewModel.onStoreBatch(batch)
                onDismiss()
            }
        }
    }

    DialogHost(
        modifier = Modifier
            .width(420.dp)
            .wrapContentHeight()
            .background(DialogBg, RoundedCornerShape(20.dp)),
        onDismissRequest = onDismiss,
        useImePadding = true
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "Add Stock",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Specify the expiry date and amount you wish to add.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(Modifier.height(6.dp))

            DateField(
                fieldModifier = Modifier.focusRequester(focusExpiry),
                header = "Expiry Date",
                placeholder = "MM/DD/YYYY",
                value = dateValue,
                onValueChange = { dateValue = it },
                onValidityChange = { isFormatValid ->
                    val parsed = runCatching {
                        LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                    }.getOrNull()
                    validDate = isFormatValid && parsed?.isAfter(LocalDate.now()) == true
                },
                onDone = { if (validDate) focusUnit.requestFocus() }
            )

            FloatField(
                fieldModifier = Modifier.focusRequester(focusUnit),
                label = "Unit",
                placeholder = "0",
                value = unit,
                onValueChange = { value ->
                    onUnitChange(
                        unit = value,
                        threshold = itemModel.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                onValidityChange = { validUnit = it },
                onDone = { if(validUnit) doSubmit() }
            )

            IntField(
                fieldModifier = Modifier.focusRequester(focusSubUnit),
                value = subUnit,
                onValueChange = { value ->
                    onSubUnitChange(
                        subUnit = value,
                        threshold = itemModel.item.subUnitThreshold,
                        onUnit = { unit = it },
                        onSubUnit = { subUnit = it }
                    )
                },
                label = "Sub Unit",
                placeholder = "0",
                onValidityChange = { validUnit = it },
                doClear = true,
                onDone = { if(validUnit) doSubmit() }
            )

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, ActionBrown),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ActionBrown
                    )
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {

                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActionBrown,
                        contentColor = Color.White
                    )
                ) {
                    Text("Add Stock")
                }
            }
        }
    }
}
