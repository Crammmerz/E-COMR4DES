package com.lumi  // Updated package name for project "lumi"

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController  // Ensure this import is present
import androidx.navigation.compose.NavHost    // Ensure this import is present
import androidx.navigation.compose.composable // Ensure this import is present
import androidx.navigation.compose.rememberNavController  // Ensure this import is present

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()  // This creates the NavController
    val viewModel: LoginViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {  // NavHost sets up navigation
        composable("login") {
            LoginScreen(navController, viewModel)
        }
        composable("home/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "USER"
            HomeScreen(navController, userType, viewModel)
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedUserType by remember { mutableStateOf<UserType?>(null) }

    val loginState by viewModel.loginState.collectAsState()

    // Handle successful login
    LaunchedEffect(loginState.isLoggedIn) {
        if (loginState.isLoggedIn) {
            navController.navigate("home/${loginState.userType?.name}")  // Navigation action
            viewModel.reset() // Reset for next login
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Login Type", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            selectedUserType = UserType.ADMIN
            showDialog = true
        }) {
            Text("Admin Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            selectedUserType = UserType.USER
            showDialog = true
        }) {
            Text("User Login")
        }

        // Login Dialog
        if (showDialog && selectedUserType != null) {
            LoginDialog(
                userType = selectedUserType!!,
                onDismiss = { showDialog = false },
                onLogin = { username, password ->
                    viewModel.login(selectedUserType!!, username, password)
                },
                errorMessage = loginState.errorMessage
            )
        }
    }
}

@Composable
fun LoginDialog(
    userType: UserType,
    onDismiss: () -> Unit,
    onLogin: (String, String) -> Unit,
    errorMessage: String?
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${userType.name} Login") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onLogin(username, password)
            }) {
                Text("Login")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HomeScreen(navController: NavHostController, userType: String, viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to $userType Homepage!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            viewModel.reset()
            navController.navigate("login") {  // Navigation back to login
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}