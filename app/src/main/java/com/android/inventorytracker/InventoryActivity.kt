package com.android.inventorytracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.presentation.login.Login
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracker.presentation.main.Main
import com.android.inventorytracker.presentation.popup.notification_permission_request.NotificationPermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


@AndroidEntryPoint
class InventoryActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val loginViewModel: LoginViewModel = hiltViewModel()

            var showNotificationRequest by rememberSaveable { mutableStateOf(true) }
            InventoryTrackerTheme {
                if (showNotificationRequest) {
                    NotificationPermissionRequest(
                        onDismiss = { showNotificationRequest = false }
                    )
                }
                when(loginViewModel.loginState){
                    LoginState.LOGGED_OUT -> {
                        Login(
                            userRole = loginViewModel.userRole,
                            onSetUserRole = loginViewModel::updateUserRole,
                            onLogin = loginViewModel::onLogin
                        )
                    }
                    LoginState.LOGGED_IN -> {
                        Main()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "The Preview",
    device = "spec:width=960dp,height=600dp,dpi=240,isRound=false,orientation=landscape"
)
@Composable
fun MainPanelPreview() {
    MaterialTheme {
//        Main(db)
    }
}