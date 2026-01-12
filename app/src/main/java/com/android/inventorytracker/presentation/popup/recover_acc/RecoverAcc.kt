package com.android.inventorytracker.presentation.popup.recover_acc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.presentation.shared.component.input_fields.PinField
import com.android.inventorytracker.presentation.shared.component.input_fields.StringField
import com.android.inventorytracker.presentation.shared.component.primitive.CancelButton
import com.android.inventorytracker.presentation.shared.component.primitive.ConfirmButton
import com.android.inventorytracker.ui.theme.GoogleSans
import com.android.inventorytracker.ui.theme.Palette
import kotlinx.coroutines.launch

@Composable
fun RecoverAcc(
    onDismiss: () -> Unit,
    onShowChangePass : (Boolean) -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    var pin by rememberSaveable { mutableStateOf("") }
    var phrase by rememberSaveable { mutableStateOf("") }

    var validPin by rememberSaveable { mutableStateOf(false) }
    var validPhrase by rememberSaveable { mutableStateOf(false) }

    val valid = validPin || validPhrase

    var successChange by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val scope = rememberCoroutineScope()
    val focusNewPass = remember { FocusRequester() }
    val focusConfirmPass = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusNewPass.requestFocus()
    }

    fun verifyRecoveryInfo() {
        if (valid) {
            scope.launch {
                val success = loginViewModel.verifyRecoveryInfo(pin, phrase)
                successChange = success
                if (success) {
                    onShowChangePass(true)
                    onDismiss()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(460.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Palette.PopupSurface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Account Recovery",
                    style = TextStyle(
                        fontFamily = GoogleSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Palette.DarkBeigeText
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    PinField(
                        fieldModifier = Modifier.focusRequester(focusNewPass),
                        value = pin,
                        onValueChange = { pin = it },
                        header = "PIN",
                        onValidityChange = { validPin = it },
                        onDone = { focusConfirmPass.requestFocus() }
                    )
                    Text("or")
                    StringField(
                        fieldModifier = Modifier.focusRequester(focusConfirmPass),
                        value = phrase,
                        onValueChange = { phrase = it },
                        header = "Security Phrase",
                        placeholder = "Enter phrase",
                        onValidityChange = { validPhrase = it },
                        onDone = { verifyRecoveryInfo() },
                        showCounter = false
                    )
                }

                Column {
                    if (successChange == false) {
                        Text(
                            text = "Invalid PIN or Security Phrase",
                            style = TextStyle(
                                fontFamily = GoogleSans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = Color.Red
                        )
                    }
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CancelButton(onClick = onDismiss)
                    Spacer(modifier = Modifier.width(12.dp))
                    ConfirmButton(
                        text = "Submit",
                        containerColor = Palette.ButtonDarkBrown,
                        enabled = valid,
                        onClick = { verifyRecoveryInfo() }
                    )
                }
            }
        }
    }
}
