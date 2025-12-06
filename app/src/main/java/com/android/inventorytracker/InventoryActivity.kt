package com.android.inventorytracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.presentation.login.Login
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracker.presentation.main.Main
import com.android.inventorytracker.presentation.notification_permission_request.NotificationPermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.android.inventorytracker.data.preferences.AuthPreferences
import com.android.inventorytracker.services.notification.NotificationHelper.canPostNotifications


@AndroidEntryPoint
class InventoryActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val loginViewModel: LoginViewModel = hiltViewModel()
            val context = LocalContext.current
            val showNotificationRequest = rememberSaveable { mutableStateOf(true) }
            InventoryTrackerTheme {
                when {
                    loginViewModel.loginState == LoginState.LOGGED_OUT
                            && loginViewModel.authEnabled -> {
                        Login(
                            userRole = loginViewModel.userRole,
                            onSetUserRole = loginViewModel::updateUserRole,
                            onLogin = loginViewModel::onLogin,
                            isRoleAuthEnabled = loginViewModel.roleAuthEnabled
                        )
                    }
                    else -> {
                        Main()
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    showNotificationRequest.value &&
                    !canPostNotifications(context)
                ) {
                    NotificationPermissionRequest(
                        modifier = Modifier.fillMaxSize(),
                        onDismiss = { showNotificationRequest.value = false }
                    )
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