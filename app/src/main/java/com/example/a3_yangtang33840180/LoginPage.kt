package com.example.a3_yangtang33840180

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme
import com.example.a3_yangtang33840180.data.patients.PatientRepository
import kotlin.jvm.java

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PatientRepository.getInstance(applicationContext)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repository) as T
            }
        })[LoginViewModel::class.java]

        setContent {
            A3_YangTang33840180Theme {
                LoginUI(viewModel)
            }
        }
    }
}

@Composable
fun LoginUI(viewModel: LoginViewModel) {
    val context = LocalContext.current
    var userIdInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(44.dp))

        OutlinedTextField(
            value = userIdInput,
            onValueChange = { userIdInput = it },
            label = { Text("User ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(39.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(text = "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account on your first visit.")
        }

        Spacer(modifier = Modifier.height(39.dp))

        Button(
            onClick = {
                viewModel.validateLogin(
                    userId = userIdInput,
                    password = passwordInput,
                    onSuccess = { userId ->
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, QuestionnairePage::class.java).apply {
                            putExtra("userIdInt", userId)
                        }
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                context.startActivity(Intent(context, RegistrationPage::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}