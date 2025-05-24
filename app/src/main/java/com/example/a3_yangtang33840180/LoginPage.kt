package com.example.a3_yangtang33840180

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.a3_yangtang33840180.data.patients.PatientDatabase
import com.example.a3_yangtang33840180.ui.theme.ui.theme.A3_YangTang33840180Theme
import com.example.a3_yangtang33840180.data.patients.PatientRepository
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A3_YangTang33840180Theme {
                val database = PatientDatabase.getDatabase(applicationContext)
                val repository = PatientRepository(database.patientDao())
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        LoginUI(repository = repository)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginUI(
    repository: PatientRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userIdInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account on your first visit.")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val loginSuccessful = validateLoginInputById(
                        inputUserId = userIdInput.toIntOrNull() ?: -1,
                        inputPassword = passwordInput,
                        repository = repository
                    )

                    if (loginSuccessful) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, QuestionnairePage::class.java)
                        intent.putExtra("USER_ID", userIdInput.toInt())
                        context.startActivity(intent)
                    } else {
                        Toast
                            .makeText(context, "Invalid ID or password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
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

suspend fun validateLoginInputById(
    inputUserId: Int,
    inputPassword: String,
    repository: PatientRepository
): Boolean {
    val patient = repository.getPatientById(inputUserId)
    return patient?.passWord == inputPassword
}