package com.example.a3_yangtang33840180

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3_yangtang33840180.ui.theme.ui.theme.A3_YangTang33840180Theme
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A3_YangTang33840180Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        this@LoginPage, // Gives LoginScreen the context to access files
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(context: Context, modifier: Modifier = Modifier) { // Login Screen that verifies UserId and PhoneNumber
    var selectedUserId by remember { mutableStateOf("") } // Stores selected UserId
    var phoneNumber by remember { mutableStateOf("") } // Stores inputted PhoneNumber
    var userIds by remember { mutableStateOf(listOf<String>()) } // Stores UserIds
    var errorMessage by remember { mutableStateOf("") } // Stores error message if validation fails
    val switchContext = LocalContext.current

    var expanded by remember { mutableStateOf(false) }  // Dropdown menu is collapsed by default (false)

    // Load UserId from data.csv file when screen displays
    LaunchedEffect(context) {
        val loadedUserIds = loadUserIdsFromCSV(context, "data.csv") // Load UserIds through function from data.csv
        Log.d("DEBUG", "User IDs loaded: $loadedUserIds") // Check if UserIds have been displayed
        userIds = loadedUserIds.sortedBy { it.toIntOrNull() ?: Int.MAX_VALUE } // Convey UserId strings to integers then sort in ascending order
    }

    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) // Reference to where the SP file where we store the data
    val editor = sharedPreferences.edit() // Can edit the SharedPreferences file

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text( // Login Text
            text = "Log In",
            style = androidx.compose.ui.text.TextStyle(fontSize = 40.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp)) // Space under login text

        // Show dropdown for UserIds
        ExposedDropdownMenuBox(
            expanded = expanded, // Represents whether dropdown is open or closed
            onExpandedChange = { expanded = !expanded } // Switches the states when called
        )
        {
            OutlinedTextField(
                value = selectedUserId,
                onValueChange = { selectedUserId = it}, // Update selectedUserId when value changes
                label = { Text("My ID (Provided by your Clinician)") }, // TextField for the selected box
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // Add the dropdown to the text field
                readOnly = true, // Make text field read only
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )

            // Dropdown menu for the user IDs
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false } // Close dropdown if clicked outside
            ) {
                userIds.forEach { id -> // Loop through all UserIds
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            selectedUserId = id // When UserId is clicked, set it as the selected UserId
                            expanded = false // Close the dropdown menu

                            editor.putString("userId", id).apply() // Save selected UserId in SharedPreferences

                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField( // Phone number input field
            value = phoneNumber, // Shows current phone number
            onValueChange = { phoneNumber = it },
            label = { Text(text = "Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Hide the text
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Set the keyboard to be for typing numbers
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage, // Display the error message
                color = MaterialTheme.colorScheme.error, // Make the message in red
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text( // Informing users its only for pre-registered
            text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing.",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )

        Button( // Continue button to next screen
            onClick = {
                val isValid = validateUser(context, "data.csv", selectedUserId, phoneNumber) // Validating user credentials
                if (isValid) {
                    switchContext.startActivity(Intent(switchContext, QuestionnairePage::class.java)) // Goes to next screen if valid
                } else {
                    errorMessage = "Invalid User ID or Phone Number." // Shows error message if invalid
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                context.startActivity(Intent(context, RegisterationPage()::class.java))
            }
        ) {
            Text("Register")
        }
    }
}

// Function to retrieve the UserId values from data.csv file
fun loadUserIdsFromCSV(context: Context, fileName: String): List<String> {
    val userIds = mutableListOf<String>() // List to hold UserId
    try {
        context.assets.open(fileName).bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->  // Don't read the header row so start from row 2 [index 1]
                val values = line.split(",").map { it.trim() } // Split the line by commas and remove extra spaces
                if (values.isNotEmpty() && values[1].isNotBlank()) {
                    userIds.add(values[1])  // Add the second column UserId, to the list

                    val userGender = values[2].trim() //Get gender info from column 3, trim any whitespaces
                    val userId = values[1]

                    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit() // Edit the shared preferences so we can save stuff inside

                    // Get total HEIFA score for the correct gender
                    val totalScoreMale = values[3].toFloatOrNull() ?: 0f // Get total HEIFA score for males in Column 4 (0 if value missing)
                    val totalScoreFemale = values[4].toFloatOrNull() ?: 0f // Get total HEIFA score for females in Column 5

                    // Check userGender and store if aligned
                    if (userGender.equals("Female", ignoreCase = true)) { // Check if userGender is female, ignore whether capitalised or not
                        editor.putFloat("HEIFAtotalscore_${userId}_Female", totalScoreFemale) // Save score if true
                        Log.d("DEBUG", "Stored Female score for user $userId: $totalScoreFemale") // Check if score is saved

                    } else if (userGender.equals("Male", ignoreCase = true)) { // Check if userGender is female, ignore whether capitalised or not
                        editor.putFloat("HEIFAtotalscore_${userId}_Male", totalScoreMale) // Save score if true
                        Log.d("DEBUG", "Stored Male score for user $userId: $totalScoreMale") // Check if score is saved
                    }

                    editor.putString("Sex_${userId}", userGender).apply() // Save user gender with their Id

                }
            }
        }
    } catch (e: Exception) {
        Log.e("ERROR", "Failed to load user IDs", e) // Check if something goes wrong when loading data
    }
    return userIds // Return list of loaded UserIds so value can be used outside function
}


// Check user credentials from data.csv
fun validateUser(context: Context, fileName: String, userId: String, phone: String): Boolean { // Boolean checks if true or false
    try {
        val inputStream = context.assets.open(fileName) // Open data.csv file from assets folder

        // Reads the data from the data.csv file
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line ->  // Skip first line and loop through the rest
                val values = line.split(",") // Split the line into columns
                if (values.size >= 2) { // Check if there are at least two values (for phone number and user id)
                    val csvUserId = values[1].trim() // Get UserId from data.csv and trim spaces
                    val csvPhone = values[0].trim() // Get phone number from data.csv and trim spaces

                    // If both UserId and phone match then return true
                    if (csvUserId == userId && csvPhone == phone) {
                        return true
                    }
                }
            }
        }
    } // Print error message
    catch (e: Exception) {
        e.printStackTrace()
    }
    return false  // If no match found, return false
}