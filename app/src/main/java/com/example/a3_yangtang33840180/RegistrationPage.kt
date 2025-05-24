package com.example.a3_yangtang33840180

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.a3_yangtang33840180.data.patients.PatientDatabase
import com.example.a3_yangtang33840180.data.patients.PatientRepository
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme
import kotlinx.coroutines.launch

class RegistrationPage : ComponentActivity() {
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
                        RegistrationScreen(repository = repository)
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(
    repository: PatientRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var userIdInput by remember { mutableStateOf("") }
    var phoneNumberInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register",
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
            value = phoneNumberInput,
            onValueChange = { phoneNumberInput = it },
            label = { Text("Phone Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
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

        Button(
            onClick = {
                coroutineScope.launch {
                    val userId = userIdInput.toIntOrNull()
                    val phoneNumber = phoneNumberInput.toLongOrNull()

                    if (userId == null || phoneNumber == null) {
                        Toast.makeText(context, "Invalid ID or phone number", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val existingPatient = repository.getPatientById(userId)

                    if (existingPatient != null && existingPatient.phoneNumber == phoneNumber) {
                        val updatedPatient = existingPatient.copy(
                            name = nameInput,
                            passWord = passwordInput
                        )
                        repository.updatePatient(updatedPatient)

                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, LoginPage::class.java))
                    } else {
                        Toast.makeText(context, "User ID and phone number do not match", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}