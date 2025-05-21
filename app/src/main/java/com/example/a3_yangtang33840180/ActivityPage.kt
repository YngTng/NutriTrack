package com.example.a3_yangtang33840180

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
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
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a3_yangtang33840180.fruityVice.FruitViewModel
import com.example.a3_yangtang33840180.genAI.GenAIScreen

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
                val selectedItemState = remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { // Defines the bottom navigation bar
                        BottomBar(navController = navController, selectedItemState = selectedItemState)
                    }
                ){ innerPadding -> // Makes sure the layout isn't hidden under bottom bar
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MyNavHost(navController, selectedItemState)
                    }
                }
            }
        }
    }
}

@Composable
fun MyNavHost(navController: NavHostController, selectedItemState: MutableState<Int>) {
    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home") {
            HomePage()
        }
        // Define the composable for the "insights" route
        composable("Insights") {
            InsightsPage(navController, selectedItemState)
        }
        // Define the composable for the "insights" route
        composable("NutriCoach") {
            NutricoachPage()
        }
        // Define the composable for the "settings" route
        composable("Settings") {
            SettingsPage(navController)
        }
        // Define the composable for the "clinician login" route
        composable("ClinicianLogin") {
            ClinicianLoginPage(navController)
        }
        // Define the composable for the "clinician stats" route
        composable("ClinicianDashboard") {
            ClinicianDashboardPage(navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, selectedItemState: MutableState<Int>) {

    val items = listOf(
        "Home",
        "Insights",
        "NutriCoach",
        "Settings"
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when(item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insights" -> Icon(Icons.Filled.Info, contentDescription = "Insights")
                        "NutriCoach" -> Icon(Icons.Filled.Face, contentDescription = "NutriCoach")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                label = { Text(item) },
                selected = selectedItemState.value == index,
                onClick = {
                    selectedItemState.value = index
                    navController.navigate(item)
                }
            )
        }
    }
}

// Function for the Home page that displays the user food quality score
@Composable
fun HomePage() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Need to retrieve values from sharedPreferences to display user Id and need currentUserGender to retrieve the corresponding gender score
    val currentUserID = sharedPreferences.getString("currentUserID", "Unknown") ?: "Unknown"
    val currentUserGender = sharedPreferences.getString("Sex_${currentUserID}", "Unknown") ?: "Unknown"
    val currentUserScore = sharedPreferences.getFloat("HEIFApossibleScoreTotalscore_${currentUserID}_$currentUserGender", 0f)

    // Check if values have been correctly retrieved
    Log.d("DEBUG", "User ID: $currentUserID, Gender: $currentUserGender, Total Score: $currentUserScore")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ){

        Spacer(modifier = Modifier.height(15.dp)) // Add space to the top of the column

        // Greeting the user
        Text(
            text = "Hello,",
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "User $currentUserID",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Row for the information about changing details & the edit button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "You've already filled in your Food\nIntake Questionnaire, but you can\nchange details here:",
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
            ){
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edits",
                    Modifier.padding(end = 6.dp)
                )
                Text("Edit")
            }
        }

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        // Add image
        Image(
            painter = painterResource(id = R.drawable.plate),
            contentDescription = "Plate",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        // Display current user score
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
            ){
                Text(
                    text = "$currentUserScore / 100", // Displays their total score based on their user id and gender out of 100
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

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        Text( // Providing information about the food quality score
            text = "What is the Food Quality Score?",
            style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp)) // Add space

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\nThis personalised measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp)
        )
    }
}

// Function for the Insights page to display the progress bars
@Composable
fun InsightsPage(navController: NavHostController, selectedItemState: MutableState<Int> = remember { mutableIntStateOf(0) }) {
    // Retrieve the currentUserID and currentUserGender
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val currentUserID = sharedPreferences.getString("currentUserID", "Unknown") ?: "Unknown"
    val currentUserGender = sharedPreferences.getString("Sex_$currentUserID", "Unknown") ?: "Unknown"

    Log.d("DEBUG", "Slider User ID: $currentUserID, Gender: $currentUserGender") // Check if the correct values have been retrieved

    val columnHeaders = listOf("Discretionary", "Vegetables", "Fruit", "Grains/Cereals", "Wholegrains", "Meat", "Dairy", "Sodium", "Alcohol", "Water", "Sugar", "Saturated fat", "Unsaturated fat")
    val possibleScoreTotals = listOf("10.0", "10.0", "10.0", "5.0", "5.0", "10.0", "10.0", "10.0", "5.0", "5.0", "10.0", "5.0", "5.0")

    val (filteredColumns, userValues) = remember {
        genderFilteredColumns(context, currentUserID, currentUserGender)  // Lists column names based on the user gender
    }

    Column( // Having a column to put all the UI elements inside
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

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
            val maxProgress = possibleScoreTotals.getOrNull(index)?.toFloat() ?: 10f // Call the max values and default to 10 if none

            // Defining my colours for the track
            val inactiveTrack = colorResource(id = R.color.purple_200)
            val activeTrack = colorResource(id = R.color.purple_500)

            Row( // Put the column name, progress bar and possibleScoreTotals in the same row
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = columnHeaders.getOrNull(index) ?: "", // Retrieve column names then display, "" if there's null
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
                    text = "${progress}/${possibleScoreTotals.getOrNull(index) ?: ""}",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
        TotalValues(context, currentUserID, currentUserGender) // Display total values for each stat

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Center buttons horizontally
            verticalArrangement = Arrangement.spacedBy(5.dp) // Add space between buttons
        ){
            //NutriCoach Page
            Button(
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp),
                onClick = {
                    selectedItemState.value = 2
                    navController.navigate("NutriCoach") // Navigate to the NutriCoach page
                }
            ){
                Image( //Icon for nutricoach button
                    painter = painterResource(id = R.drawable.rocket),
                    contentDescription = "Rocket Image",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                )
                Text("Improve my diet!")
            }
        }
    }
}

// Function for the NutriCoach page to be implemented in the future
@Composable
fun NutricoachPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "NutriCoach",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ){
            //#TODO Add NutriCoach page content
            SearchFruits()


            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 18.dp),
                thickness = 3.dp,        // Thin colour
                color = Color.LightGray  // Light colour
            )

            //#TODO Add NutriCoach page content
            GenAIScreen()
        }
    }
}

// Function for the Settings Page
@Composable
fun SettingsPage(navController: NavHostController) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text( // Providing information about the food quality score
            text = "Settings",
            style = androidx.compose.ui.text.TextStyle(fontSize = 25.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ){
            Text( // Providing information about the food quality score
                text = "ACCOUNT",
                style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Outlined.Phone, contentDescription = "Phone")

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                //#TODO Add Settings page content

            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.idbadge),
                    contentDescription = "ID Badge",
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                //#TODO Add Settings page content
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Default.Person, contentDescription = "Log In")

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                //#TODO Add Settings page content
            }


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                thickness = 3.dp,        // Divider Thickness
                color = Color.LightGray  // Grey Colour
            )

            Text( // Providing information about the food quality score
                text = "OTHER SETTINGS",
                style = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    context.startActivity(Intent(context, LoginPage::class.java))
                }
            ){
                Icon(Icons.Default.ExitToApp, contentDescription = "Log Out")

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                Text("Logout")
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow Right")
                }
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("ClinicianLogin")
                }
            ){
                Icon(Icons.Default.Person, contentDescription = "Log In")

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                Text("Clinician Login")
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Arrow Right")
                }
            }
        }
    }
}

@Composable
fun ClinicianLoginPage(navController: NavHostController) {
    var clinicianKey by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) { // Show alert dialog if login failed
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Login Failed") },
            text = { Text("Invalid Key") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Clinician Login", fontSize = 26.sp, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = clinicianKey,
            onValueChange = { clinicianKey = it },
            label = { Text("Enter your clinician key") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(45.dp))

        Button( // Login button
            onClick = {
                //#TODO Create Credential validation against database
                if (clinicianKey == "admin123") {
                    navController.navigate("ClinicianDashboard")
                } else {
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ){ // Button Content
            Icon(Icons.Default.ExitToApp, contentDescription = "Log In") // Icon for login button

            Spacer(modifier = Modifier.width(15.dp)) // Add space between content

            Text("Clinician Login")
        }
    }
}

@Composable
fun ClinicianDashboardPage(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Clinician Dash",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ){
            //#TODO Create Dashboard

            //Clinician Logout button
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("Settings")
                }
            ){
                Text("Done")
            }
        }
    }
}

// Function to get the column information based on the currentUserGender
fun genderFilteredColumns(context: Context, currentUserID: String, currentUserGender: String): Pair<List<String>, List<String>> {
    val filteredColumns = mutableListOf<String>()
    val userValues = mutableListOf<String>()

    try { // Open the CSV file from assets
        val reader = BufferedReader(InputStreamReader(context.assets.open("data.csv")))
        val lines = reader.readLines() // Read all the lines from the data.csv file

        if (lines.isNotEmpty()) {
            val tableHeaders = lines.first().split(",").map { it.trim() } // Get the first line the tableHeaders
            val relevantIndices = mutableListOf<Int>() // List to store the column indices based on the user gender's food categories

            // Find gender-specific columns
            tableHeaders.forEachIndexed { index, column ->
                if (currentUserGender.equals("Female", ignoreCase = false) && column.endsWith("Female", ignoreCase = false)) { // Ignore case false to check for upper and lower case
                    if (index != 4) { // Ignore the HEIFApossibleScoreTotalscoreFemale but append all the other columns that end with "Female"
                        filteredColumns.add(column) // Stored column names
                        relevantIndices.add(index) // Stored column indices
                    }
                } else if (currentUserGender.equals("Male", ignoreCase = false) && column.endsWith("Male", ignoreCase = false)) {
                    if (index != 3) { // Ignore the HEIFApossibleScoreTotalscoreMale but append all the other columns that end with "Male"
                        filteredColumns.add(column)
                        relevantIndices.add(index)
                    }
                }
            }

            // Find the corresponding row for the currentUserID
            val currentUserRow = lines.drop(1).find { line ->
                val currentUserData = line.split(",").map { it.trim() }
                currentUserData.size > 1 && currentUserData[1] == currentUserID  // Check the second column for the currentUserID
            }?.split(",") ?: emptyList()

            Log.d("CSVProcessor", "User Row: $currentUserRow") // Check if the correct row is being retrieved for the user

            if (currentUserRow.isNotEmpty()) { // Find relevant values for each column based on the gender
                relevantIndices.forEach { index ->
                    if (!(currentUserGender.equals("male", ignoreCase = true) && index == 3) &&
                        !(currentUserGender.equals("female", ignoreCase = true) && index == 4)) {
                        userValues.add(
                            currentUserRow.getOrNull(index)?.trim() ?: "0"
                        )
                    }
                }

            } else {
                Log.e("CSVProcessor", "User row not found for currentUserID: $currentUserID")
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
fun TotalValues(context: Context, currentUserID: String, currentUserGender: String) {
    var genderedScore by remember { mutableStateOf(0f) }

    try { // Open the CSV file from assets, try block to handle potential errors
        val reader = BufferedReader(InputStreamReader(context.assets.open("data.csv")))
        val lines = reader.readLines()

        Log.d("CSVProcessor", "CSV file content: $lines") // Log the entire CSV content to check

        if (lines.isNotEmpty()) { // Make sure file not empty
            val tableHeaders = lines.first().split(",").map { it.trim() } // Get tableHeaders from the first row
            Log.d("CSVProcessor", "Headers: $tableHeaders") // Check if tableHeaders are retrieved

            // Find the currentUserID row
            val currentUserRow = lines.drop(1).find { line ->
                val currentUserData = line.split(",").map { it.trim() }
                currentUserData.size > 1 && currentUserData[1] == currentUserID // Check if currentUserID is in the second column
            }?.split(",") ?: emptyList() // If user row found then remove its commas otherwise return an empty list

            Log.d("CSVProcessor", "User Row: $currentUserRow")  // Check if rows are retrieved

            // Find the column index based on gender
            if (currentUserRow.isNotEmpty()) {
                var genderedScoreString: String? = null // Initialise variable to store gender value

                if (currentUserGender.equals("Male", ignoreCase = true)) { // Access the 4th column if Male
                    genderedScoreString = currentUserRow.getOrNull(3)?.trim()
                }
                else if (currentUserGender.equals("Female", ignoreCase = true)) { // Access the 5th column if Male
                    genderedScoreString =
                        currentUserRow.getOrNull(4)?.trim() // 5th column for female
                }

                Log.d("CSVProcessor", "Gender-specific value string for $currentUserGender: $genderedScoreString") // Check the HEIFA gender total value retrieved

                genderedScore = genderedScoreString?.toFloatOrNull() ?: 0f // Convert string to float
            } else {
                Log.e("CSVProcessor", "User row not found for currentUserID: $currentUserID")
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
        ){
            var progress by remember { mutableStateOf(genderedScore) } // Mutable variable to hold HEIFA gender value
            
            val inactiveTrack = colorResource(id = R.color.purple_200)
            val activeTrack = colorResource(id = R.color.purple_500)

            // Use launched effect to make sure the slider stays the same
            LaunchedEffect(genderedScore) {
                progress = genderedScore // Keeps the progress value to the gender specific value
            }

            LinearProgressIndicator( // Progress bars
                progress = { progress / 100f },
                modifier = Modifier
                    .weight(5f)
                    .height(6.dp),
                color = activeTrack,
                trackColor = inactiveTrack
            )

            Spacer(modifier = Modifier.width(10.dp)) // Add space

            Text( // Displays the score value out of 100
                text = "${progress}/100",
                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
        }

        val context = LocalContext.current
        val shareScore = "My total food quality score is $genderedScore!" // Sharing message for the gender specific value

        Spacer(modifier = Modifier.height(20.dp)) // Add space

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Center buttons horizontally
            verticalArrangement = Arrangement.spacedBy(5.dp) // Add space between buttons
        ){
            Button( // Share button to send food quality using an intent
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareScore) // Add the share text
                    }
                    // Start the activity to share the text
                    context.startActivity(Intent.createChooser(shareIntent, "Share with someone"))
                },
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp) // Makes the button smaller
            ){
                Icon( // Icon for the share button
                    Icons.Filled.Share,
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                )
                Text("Share with someone") // Button text
            }
        }
    }
}

@Composable
fun SearchFruits(viewModel: FruitViewModel = viewModel()) {
    var input by remember { mutableStateOf("") }

    val fruit by viewModel.fruit.collectAsState()
    val error by viewModel.error.collectAsState()

    Column{
        Text( // Providing information about the food quality score
            text = "Fruit Name",
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(
                modifier = Modifier.sizeIn(minWidth = 200.dp, minHeight = 25.dp),
                value = input,
                onValueChange = { input = it },
                label = { Text("Enter fruit name") },
                shape = RoundedCornerShape(35.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Button(
                    modifier = Modifier.sizeIn(maxWidth = 120.dp, minHeight = 25.dp),
                    onClick = { viewModel.fetchFruit(input) }) {
                    Icon(Icons.Outlined.Search, contentDescription = "Search")

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Details")
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        fruit?.let {
            Text(
                text = "Name: ${it.name}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Genus: ${it.genus}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Family: ${it.family}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Order: ${it.order}",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Nutrition:",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Calories: ${it.nutritions.calories}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Carbs: ${it.nutritions.carbohydrates}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Protein: ${it.nutritions.protein}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fat: ${it.nutritions.fat}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sugar: ${it.nutritions.sugar}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}