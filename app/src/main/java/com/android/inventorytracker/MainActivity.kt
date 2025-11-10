package com.android.inventorytracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.android.inventorytracker.data.local.database.InventoryDatabase
import com.android.inventorytracker.data.model.LoginState
import com.android.inventorytracker.data.repository.UserRepository
import com.android.inventorytracker.presentation.login.Login
import com.android.inventorytracker.presentation.login.viewmodel.LoginViewModel
import com.android.inventorytracker.ui.theme.InventoryTrackerTheme
import com.android.inventorytracker.presentation.main.Main

class MainActivity : ComponentActivity() {

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val db = InventoryDatabase.getDatabase(context)
            val userRepository = UserRepository(db.userDao())
            val loginViewModel = LoginViewModel(userRepository)
            InventoryTrackerTheme {
                when(loginViewModel.loginState){
                    LoginState.LOGGED_OUT -> {
                        Login(
                            userRole = loginViewModel.userRole,
                            onSetUserRole = loginViewModel::updateUserRole,
                            onLogin = loginViewModel::onLogin
                        )
                    }
                    LoginState.LOGGED_IN -> Main(db = db)
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