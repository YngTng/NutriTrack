package com.example.a3_yangtang33840180

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme
import java.io.BufferedReader
import java.io.InputStreamReader

class ActivityPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A3_YangTang33840180Theme {
                val navController: NavHostController = rememberNavController() // Keeps track of current screen

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { // Defines the bottom navigation bar
                        BottomBar(navController)
                    }
                ) { innerPadding -> // Makes sure the layout isn't hidden under bottom bar
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MyNavHost(innerPadding, navController)
                    }
                }
            }
        }
    }
}

// Nav Host Defines navigation graph which tells the app what screens I have
@Composable
fun MyNavHost(innerPadding: PaddingValues, navController: NavHostController) {
    // NavHost composable to define the navigation graph
    NavHost(
        navController = navController,
        startDestination = "Home" // Set the starting destination to "home"
    ) {
        // Define the composable for the "home" route
        composable("Home") {
            HomePage()
        }
        // Define the composable for the "insights" route
        composable("Insights") {
            InsightsPage(modifier = Modifier.fillMaxSize())
        }
        // Define the composable for the "insights" route
        composable("NutriCoach") {
            NutriCoachPage(innerPadding)
        }
        // Define the composable for the "settings" route
        composable("Settings") {
            SettingsPage(innerPadding)
        }
    }
}

// Bottom navigation bar to switch between screens
@Composable
fun BottomBar(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) } // Tracks which item is currently selected
    val items = listOf("Home", "Insights", "NutriCoach", "Settings")

    NavigationBar(
        modifier = Modifier.height(80.dp)
    ) {
        // Iterate through each item
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { // Choosing icons for navigation bar pages
                    when (item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insights" -> Icon(Icons.Filled.AccountBox, contentDescription = "Insights")
                        "NutriCoach" -> Icon(Icons.Filled.Face, contentDescription = "NutriCoach")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                label = { Text(item, modifier = Modifier.padding(start = 1.dp)) },   // Using the item's name as the label

                selected = selectedItem == index, // Check if the item is currently selected

                onClick = {
                    selectedItem = index // Update selected item state to the current index
                    navController.navigate(item) // Navigate to corresponding screen based on item name
                }
            )
        }
    }
}

// Function for the Home page that displays the user food quality score
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Need to retrieve values from sharedPreferences to display user Id and need userGender to retrieve the corresponding gender score
    val userId = sharedPreferences.getString("userId", "Unknown") ?: "Unknown"
    val userGender = sharedPreferences.getString("Sex_${userId}", "Unknown") ?: "Unknown"
    val totalScore = sharedPreferences.getFloat("HEIFAtotalscore_${userId}_$userGender", 0f)

    // Check if values have been correctly retrieved
    Log.d("DEBUG", "User ID: $userId, Gender: $userGender, Total Score: $totalScore")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(15.dp)) // Add space to the top of the column

        // Greeting the user
        Text(
            text = "Hello,",
            style = androidx.compose.ui.text.TextStyle(fontSize = 25.sp)
        )
        Text(
            text = "User $userId",
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Row for the information about changing details & the edit button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    context.startActivity(Intent(context, QuestionnairePage::class.java)) // Takes user to previous page to edit their details
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 8.dp)
                    .height(40.dp)
            ) {
                Text(text = "Edit") // Button text
            }
        }

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        // Add image
        Image(
            painter = painterResource(id = R.drawable.plate),
            contentDescription = "Fit Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        // Telling user their user score
        Text(
            text = "My Score",
            style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(7.dp)) // Add space

        Row(
            verticalAlignment = Alignment.CenterVertically) {

            Image( // App logo
                painter = painterResource(id = R.drawable.rounduparrow),
                contentDescription = "Up Arrow",
                modifier = Modifier.size(25.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Your Food Quality Score",
                fontSize = 16.sp
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "$totalScore / 100", // Displays their total score based on their user id and gender out of 100
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add space

        // Add a light divider
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,        // Thin colour
            color = Color.LightGray  // Light colour
        )

        Spacer(modifier = Modifier.height(10.dp)) // Adds space

        Text( // Providing information about the food quality score
            text = "What is the Food Quality Score?",
            style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp)) // Adds space

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\nThis personalised measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
        )
    }
}

// Function for the NutriCoach page to be implemented in the future
@Composable
fun NutriCoachPage(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "NutriCoach Page",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("To be implemented in the future.")
    }
}

// Function for the Settings page to be implemented in the future
@Composable
fun SettingsPage(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings Page",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("To be implemented in the future.")
    }
}

// Function for the Insights page to display the progress bars
@Composable
fun InsightsPage(modifier: Modifier) {
    // Retrieve the userId and userGender
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("userId", "Unknown") ?: "Unknown"
    val userGender = sharedPreferences.getString("Sex_$userId", "Unknown") ?: "Unknown"

    Log.d("DEBUG", "Slider User ID: $userId, Gender: $userGender") // Check if the correct values have been retrieved

    val columnNames = listOf("Discretionary", "Vegetables", "Fruit", "Grains/Cereals", "Wholegrains", "Meat", "Dairy", "Sodium", "Alcohol", "Water", "Sugar", "Saturated fat", "Unsaturated fat")
    val totals = listOf("10.0", "10.0", "10.0", "5.0", "5.0", "10.0", "10.0", "10.0", "5.0", "5.0", "10.0", "5.0", "5.0")

    val (filteredColumns, userValues) = remember {
        getColumnsBasedOnGender(context, userId, userGender)  // Lists column names based on the user gender
    }

    Column( // Having a column to put all the UI elements inside
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(50.dp)) // Add space

        Text( // Displaying the title
            text = "Insights: Food Score",
            style = androidx.compose.ui.text.TextStyle(fontSize = 25.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp)) // Add space

        // Show the progress bars for each food category based on user gender
        filteredColumns.forEachIndexed { index, column ->
            var progress by remember { mutableStateOf(userValues.getOrNull(index)?.toFloatOrNull() ?: 0f) } // Holds the value of the category
            val maxProgress = totals.getOrNull(index)?.toFloat() ?: 10f // Call the max values and default to 10 if none

            // Defining my colours for the track
            val inactiveTrack = colorResource(id = R.color.purple_200)
            val activeTrack = colorResource(id = R.color.purple_500)

            Row( // Put the column name, progress bar and totals in the same row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = columnNames.getOrNull(index) ?: "", // Retrieve the column names and display nothing "" if there's nothing
                    style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.Bold
                )

                LaunchedEffect(userValues.getOrNull(index)) {// Updates progress variable if something changes
                    progress = userValues.getOrNull(index)?.toFloatOrNull() ?: 0f
                }

                LinearProgressIndicator( // Calling the progress bars
                    progress = { progress / maxProgress },
                    modifier = Modifier
                        .weight(3f)
                        .height(5.dp),
                    color = activeTrack, // Choosing the color for the filled section
                    trackColor = inactiveTrack // Choosing the color for the unfilled value
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${progress}/${totals.getOrNull(index) ?: ""}",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
        TotalValue(context, userId, userGender) // Display total value
    }
}

// Function to get the column information based on the userGender
fun getColumnsBasedOnGender(context: Context, userId: String, userGender: String): Pair<List<String>, List<String>> {
    val filteredColumns = mutableListOf<String>()
    val userValues = mutableListOf<String>()

    try { // Open the CSV file from assets
        val inputStream = context.assets.open("data.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines() // Read all the lines from the data.csv file

        if (lines.isNotEmpty()) {
            val headers = lines.first().split(",").map { it.trim() } // Get the first line the headers
            val relevantIndices = mutableListOf<Int>() // List to store the column indices based on the user gender's food categories

            // Find gender-specific columns
            headers.forEachIndexed { index, column ->
                if (userGender.equals("Female", ignoreCase = false) && column.endsWith("Female", ignoreCase = false)) { // Ignore case false to check for upper and lower case
                    if (index != 4) { // Ignore the HEIFAtotalscoreFemale but append all the other columns that end with "Female"
                        filteredColumns.add(column) // Stored column names
                        relevantIndices.add(index) // Stored column indices
                    }
                } else if (userGender.equals("Male", ignoreCase = false) && column.endsWith("Male", ignoreCase = false)) {
                    if (index != 3) { // Ignore the HEIFAtotalscoreMale but append all the other columns that end with "Male"
                        filteredColumns.add(column)
                        relevantIndices.add(index)
                    }
                }
            }

            // Find the corresponding row for the userId
            val userRow = lines.drop(1).find { line ->
                val rowValues = line.split(",").map { it.trim() }
                rowValues.size > 1 && rowValues[1] == userId  // Check the second column for the userId
            }?.split(",") ?: emptyList()

            Log.d("CSVProcessor", "User Row: $userRow") // Check if the correct row is being retrieved for the user

            if (userRow.isNotEmpty()) { // Find the relevant values for each column baed on the gender indices
                relevantIndices.forEach { index ->
                    if (!(userGender.equals("male", ignoreCase = true) && index == 3) &&
                        !(userGender.equals("female", ignoreCase = true) && index == 4)) {
                        userValues.add(
                            userRow.getOrNull(index)?.trim() ?: "0"
                        )
                    }
                }

            } else {
                Log.e("CSVProcessor", "User row not found for userId: $userId")
            }

            Log.d("CSVProcessor", "Filtered Columns: $filteredColumns") // Column names based on user gender
            Log.d("CSVProcessor", "User Values: $userValues") // User values
        }
        reader.close() // Close reader after processing the data.csv
    } catch (e: Exception) {
        Log.e("CSVReader", "Error reading CSV file", e)
    }
    return Pair(filteredColumns, userValues) // Return filtered column and user values as a pair
}

// Function to show HEIFAtotalvaluegender
@Composable
fun TotalValue(context: Context, userId: String, userGender: String) {
    var genderSpecificValue by remember { mutableStateOf(0f) } // Mutable variable for the HEIFAtotalsvaluegender progress bars

    try { // Open the CSV file from assets, try block to handle potential errors
        val inputStream = context.assets.open("data.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines()

        Log.d("CSVProcessor", "CSV file content: $lines") // Log the entire CSV content to check

        if (lines.isNotEmpty()) { // Make sure file not empty
            val headers = lines.first().split(",").map { it.trim() } // Get headers from the first row
            Log.d("CSVProcessor", "Headers: $headers") // Check if headers are retrieved

            // Find the userId row
            val userRow = lines.drop(1).find { line ->
                val rowValues = line.split(",").map { it.trim() }
                rowValues.size > 1 && rowValues[1] == userId // Check if userId is in the second column
            }?.split(",") ?: emptyList() // If user row found then remove its commas otherwise return an empty list

            Log.d("CSVProcessor", "User Row: $userRow")  // Check if rows are retrieved

            // Find the column index based on gender
            if (userRow.isNotEmpty()) {
                var genderSpecificValueString: String? = null // Initialise variable to store gender vaue

                if (userGender.equals("Male", ignoreCase = true)) { // Access the 4th column if Male
                    genderSpecificValueString = userRow.getOrNull(3)?.trim()
                }
                else if (userGender.equals("Female", ignoreCase = true)) { // Access the 5th column if Male
                    genderSpecificValueString =
                        userRow.getOrNull(4)?.trim() // 5th column for female
                }

                Log.d("CSVProcessor", "Gender-specific value string for $userGender: $genderSpecificValueString") // Check the HEIFA gender total value retrieved

                genderSpecificValue = genderSpecificValueString?.toFloatOrNull() ?: 0f // Convert string to float
            } else {
                Log.e("CSVProcessor", "User row not found for userId: $userId")
            }
        }
        reader.close() // Close the reader
    } catch (e: Exception) {
        Log.e("CSVReader", "Error reading CSV file", e)
    }

    // UI to show slider and HEIFA gender total value
    Column(
        modifier = Modifier.padding(1.dp)) {

        Spacer(modifier = Modifier.height(45.dp)) // Add space

        Text(
            text = "Total Food Quality Score",
            style = androidx.compose.ui.text.TextStyle(fontSize = 17.sp),
            fontWeight = FontWeight.Bold
        )

        Row( // Row to hold the HEIFA gender column name, progress bar and value
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var progress by remember { mutableStateOf(genderSpecificValue) } // Mutable variable to hold HEIFA gender value

            // My colours for the progress bar
            val inactiveTrack = colorResource(id = R.color.purple_200)
            val activeTrack = colorResource(id = R.color.purple_500)

            // Use launched effect to make sure the slider stays the same
            LaunchedEffect(genderSpecificValue) {
                progress = genderSpecificValue // Keeps the progress value to the gender specific value
            }

            LinearProgressIndicator( // Progress bars
                progress = { progress / 100f },
                modifier = Modifier
                    .weight(5f) // Adjusts length
                    .height(6.dp), // Adjusts thickness
                color = activeTrack, // Filled bar
                trackColor = inactiveTrack // Unfilled bar
            )

            Spacer(modifier = Modifier.width(10.dp)) // Adds space

            Text( // Displays the gender value out of 100
                text = "${progress}/100",
                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
        }

        val context = LocalContext.current
        val shareText = "Hi friend! My total food quality score is $genderSpecificValue!" // Sharing message for the gender specific value

        Spacer(modifier = Modifier.height(20.dp)) // Adds space

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Center buttons horizontally
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adds space between buttons
        ) {
            Button( // Share button to send food quality using an intent
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText) // Add the share text
                    }
                    // Start the activity to share the text
                    context.startActivity(Intent.createChooser(shareIntent, "Share with someone"))
                },
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp) // Makes the button smaller
            ) {
                Text("Share with someone") // Button text
            }

            // Improve diet button (to be implemented in the future)
            Button(
                onClick = {
                    Log.d("ButtonClicked", "Improve my diet")
                },
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp) // Makes the button smaller
            ) {
                Text("Improve my diet") // Button text
            }
        }
    }
}