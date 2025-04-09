package com.example.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("welcome/{username}") { backStackEntry ->
                        val user = backStackEntry.arguments?.getString("username")
                        WelcomeScreen(user ?: "Invitado")
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error, color = MaterialTheme.colors.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Los campos no pueden estar vacíos"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error = "Formato de correo incorrecto"
                } else {
                    error = ""
                    val username = email.substringBefore("@")
                    navController.navigate("welcome/$username")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Composable
fun WelcomeScreen(user: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("¡Bienvenido, $user!", style = MaterialTheme.typography.h4)
    }
}
