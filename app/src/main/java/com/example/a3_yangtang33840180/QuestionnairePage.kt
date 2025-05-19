package com.example.a3_yangtang33840180

import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme
import java.util.Calendar

// This class is for questionnaire+modal
class QuestionnairePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A3_YangTang33840180Theme {
                val mContext = LocalContext.current // Get the current context of the app
                val currentUserID =getCurrentUserID() // Get current user id

                Log.d("Questionnaire", "Current UserID: $currentUserID") // Check if currentUserId is correct

                // Create new SharedPreferences file for current user
                val sharedPref = mContext.getSharedPreferences("food_intake_${currentUserID}.sp", MODE_PRIVATE)

                // Runs if UserId changes and sets default values if user doesn't have any selections
                LaunchedEffect(currentUserID) {
                    if (!sharedPref.contains("dropdown_selection")) {
                        sharedPref.edit().putString("dropdown_selection", "Health Devotee").apply()
                    }
                }

                // Creating mutable values that can be changed later
                val mTime1 = remember { mutableStateOf("") }
                val mTime2 = remember { mutableStateOf("") }
                val mTime3 = remember { mutableStateOf("") }

                // Calls the timeFunction for the time boxes to choose times
                val mTimePickerDialog1 = timeFunction(mTime1, "time1_${currentUserID}"
                ) // Append userId to the time key
                val mTimePickerDialog2 = timeFunction(mTime2, "time2_${currentUserID}")
                val mTimePickerDialog3 = timeFunction(mTime3, "time3_${currentUserID}")

                // Checks the state of the checkbox
                val mCheckBoxState = remember { mutableStateOf(false) }

                // List of drop down
                val options = listOf("Health Devotee", "Mindful Eater", "Wellness Striver", "Balance Seeker", "Health Procrastinator", "Food Carefree")
                var mDropDownValue by remember { mutableStateOf(options[0]) } // Default dropdown menu option is Health Devotee

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TopNavBar()

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)  // Add padding here for content
                                .verticalScroll(rememberScrollState()), // Make content scrollable
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))

                            // Food Category Checkboxes
                            Text(
                                text = "Select Food Categories",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Checkbox for food categories
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            ) { CheckboxFunction() }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 2.dp,
                                color = Color.LightGray
                            )

                            // Persona Section
                            Text(
                                text = "Your Persona",
                                modifier = Modifier.padding(top = 16.dp),
                                fontWeight = FontWeight.Bold
                            )

                            // Explaining text for the persona options
                            Text(
                                text = "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
                                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            PersonaSelectionButtons() // Calling the persona buttons function

                            Spacer(modifier = Modifier.height(16.dp)) // Adds space between elements

                            Text(text = "Which persona best fits you?", fontWeight = FontWeight.Bold) // Asking user which persona fits them

                            // Dropdown menu to select a persona
                            DropdownMenuExample(
                                dropDownValue = mDropDownValue,
                                onDropDownValueChange = { newValue ->
                                    mDropDownValue = newValue }
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 2.dp,
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.height(15.dp))

                            // Time Picker 1
                            Text(text = "Timings", fontWeight = FontWeight.Bold)

                            // For each question, we show the text and a time box
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                    Text(
                                        text = "What time of day approx. do you normally eat your biggest meal?",
                                        style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                    )
                                }
                                Column {
                                    TimeBox(
                                        time = mTime1,
                                        onClick = { mTimePickerDialog1.show() }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Repeat for the second question
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                    Text(
                                        text = "What time of day approx. do you go to sleep at night?",
                                        style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                    )
                                }
                                Column {
                                    TimeBox(
                                        time = mTime2,
                                        onClick = { mTimePickerDialog2.show() }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Repeat for the third question
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                    Text(
                                        text = "What time of day approx. do you wake up in the morning?",
                                        style = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                    )
                                }
                                Column {
                                    TimeBox(
                                        time = mTime3,
                                        onClick = { mTimePickerDialog3.show() }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            val sharedPref = mContext.getSharedPreferences("UserPrefs_${getCurrentUserID()}.sp", MODE_PRIVATE).edit()
                            Button(onClick = {
                                Log.d("MainActivity3", "Saving times for user $currentUserID")
                                Log.d("MainActivity3", "Time1: ${mTime1.value}, Time2: ${mTime2.value}, Time3: ${mTime3.value}")

                                // Save selected time and preferences in SharedPreferences for the CurrentUserId
                                sharedPref.putString("time1_${currentUserID}", mTime1.value)
                                sharedPref.putString("time2_${currentUserID}", mTime2.value)
                                sharedPref.putString("time3_${currentUserID}", mTime3.value)
                                sharedPref.putString("dropdown_selection_${currentUserID}", mDropDownValue)
                                sharedPref.putBoolean("checkbox_${currentUserID}", mCheckBoxState.value)
                                sharedPref.apply()

                                // Go to next screen after saved
                                val intent = Intent(mContext, ActivityPage::class.java)
                                mContext.startActivity(intent)
                            }) {
                                Text(text = "Save") // Button text
                            }
                        }
                    }
                }
            }
        }
    }
}

// Gets current user id from shared preferences
@Composable
fun getCurrentUserID(): String {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Get the user id or default to "unknown" if can't be found
    val userId = sharedPreferences.getString("userId", "Unknown") ?: "Unknown"
    Log.d("UserID", "Retrieved UserID: $userId") // Log statement to verify
    return sharedPreferences.getString("userId", "Unknown") ?: "Unknown"
}


// This function is for the checkboxes arranged the 3x3 grid
@Composable
fun CheckboxFunction() {
    val mContext = LocalContext.current
    val userId = getCurrentUserID() // Get the current user id

    val sharedPref = mContext.getSharedPreferences("UserPrefs_${userId}.sp", MODE_PRIVATE)

    // List of food categories
    val foodCategories = listOf("Fruits", "Vegetables", "Grains", "Red Meat", "Seafood", "Poultry", "Fish", "Eggs", "Nuts/Seeds")

    // Initial state of each checkbox
    val checkBoxStates = remember(userId) { foodCategories.associateWith { mutableStateOf(false) } }

    // Load the saved checkbox state for current user
    LaunchedEffect(userId) {

        // Clear the state for to make sure only new UserId's data is loaded
        foodCategories.forEach { category ->
            checkBoxStates[category]?.value = false  // Clear the state first
        }

        // Then load the saved states
        foodCategories.forEach { category -> // Loop so it loads each category
            val savedState = sharedPref.getBoolean("${category}_$userId", false) // Use the category-specific key
            checkBoxStates[category]?.value = savedState
        }
    }

    // Display the checkboxes
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(foodCategories.chunked(3)) { rowItems ->

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Align checkbox and text
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox( // Checkbox for each category
                            checked = checkBoxStates[category]?.value ?: false,
                            onCheckedChange = { newState ->
                                // Update the checkbox state and save to SharedPreferences
                                checkBoxStates[category]?.value = newState
                                sharedPref.edit().putBoolean("${category}_$userId", newState).apply()
                                Log.d("CheckboxGrid", "Saved $category for user $userId: $newState")
                            }
                        )
                        Spacer(modifier = Modifier.width(2.dp)) // Small gap between checkbox and text
                        Text(text = category, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// Dropdown to let user select an option
@Composable
fun DropdownMenuExample(
    dropDownValue: String, // The current selected value in the dropdown
    onDropDownValueChange: (String) -> Unit // Function to update the dropdown value
) {
    var expanded by remember { mutableStateOf(false) } // Check whether dropdown is open or closed

    val mContext = LocalContext.current
    val userId = getCurrentUserID() // Get the logged-in user's ID
    val sharedPref = mContext.getSharedPreferences("UserPrefs_${userId}.sp", MODE_PRIVATE)


    // Load the saved dropdown value when the composable is first launched
    LaunchedEffect(userId) {
        val loadedDropDown = sharedPref.getString("dropdown_selection_${userId}", "Health Devotee")
        onDropDownValueChange(loadedDropDown ?: "Health Devotee") // Update the value
    }

    Box(modifier = Modifier.padding(16.dp)) {
        // Outlined Button with text and Dropdown icon
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = dropDownValue, modifier = Modifier.weight(1f)) // Display the selected option

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Dropdown menu that shows options when button is clicked
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val options = listOf("Health Devotee", "Mindful Eater", "Wellness Striver", "Balance Seeker", "Health Procrastinator", "Food Carefree")

            options.forEach { option -> // Each item in the dropdown menu
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onDropDownValueChange(option) // Update the selected option
                        expanded = false

                        // Save selected value in SharedPreferences
                        sharedPref.edit().putString("dropdown_selection_${userId}", option).apply()
                        Log.d("DropdownMenuExample", "Saved for user $userId: $option")
                    }
                )
            }
        }
    }
}

// Time function to allow user to choose a time
@Composable
fun timeFunction(mTime: MutableState<String>, timeKey: String): TimePickerDialog {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()

    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.MINUTE)

    val userId = getCurrentUserID() // Get the logged-in user's ID
    val sharedPref = mContext.getSharedPreferences("UserPrefs_${userId}.sp", MODE_PRIVATE)

    // Load saved time when the composable is launched
    LaunchedEffect(timeKey) {
        val keyWithUserId = "${userId}_$timeKey" // Append the userId to the timeKey
        val loadedTime = sharedPref.getString(keyWithUserId, "12:00")
        mTime.value = loadedTime ?: "12:00" // Default value if none is saved
        Log.d("TimeFunction", "Loaded time for $keyWithUserId: ${mTime.value}") // Log loaded time
    }

    return TimePickerDialog(
        mContext,
        { _, selectedHour: Int, selectedMinute: Int ->
            val selectedTime = "$selectedHour:$selectedMinute"
            mTime.value = selectedTime

            // Save the selected time with the userId appended to the key
            val keyWithUserId = "${userId}_$timeKey" // Append the userId to the timeKey
            sharedPref.edit().putString(keyWithUserId, selectedTime).apply()
            Log.d("TimePicker", "Saved time for $keyWithUserId: $selectedTime")
        },
        mHour, mMinute, false
    )
}

// Display selected time in a box and let user click to choose a time
@Composable
fun TimeBox(time: MutableState<String>, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clickable(onClick = onClick)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // To make the box corners rounded not sharp
            .height(30.dp)
            .width(140.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Clock icon
            Image(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = "Clock Image",
                modifier = Modifier.size(20.dp)
            )

            // Display selected time
            Text(
                text = if (time.value.isNotEmpty()) time.value else "00:00",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = if (time.value.isNotEmpty()) Color.Black else Color.Gray // If no time then make it grey
                ),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}

// Top bar that displays "Food Intake Questionnaire" and a back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(modifier: Modifier = Modifier) {
    // For the back button press
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // Controls the scrolling behavior of the TopNavBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Creating the top bar
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Food Intake Questionnaire", // Title of app
                maxLines = 1 // Only show one line of title
            )
        },
        // Creates back button
        navigationIcon = {
            IconButton(onClick = {
                onBackPressedDispatcher?.onBackPressed() // When clicked go back
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localised description"
                )
            }
        },
        scrollBehavior = scrollBehavior, // Adds scroll behaviour to top bar
        modifier = modifier
    )
}

// Shows the persona buttons that users can click to find out more information
@Composable
fun PersonaSelectionButtons() {
    val options = listOf( // List of persona options
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )

    // Map for the persona descriptions, storing them in key value pairs
    val optionDetails = mapOf(
        "Health Devotee" to "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
        "Mindful Eater" to "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
        "Wellness Striver" to "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
        "Balance Seeker" to "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
        "Health Procrastinator" to "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
        "Food Carefree" to "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
    )

    // Map for the persona pictures
    val optionImages = mapOf(
        "Health Devotee" to R.drawable.healthdevotee,
        "Mindful Eater" to R.drawable.mindfuleater,
        "Wellness Striver" to R.drawable.wellnessstriver,
        "Balance Seeker" to R.drawable.balanceseeker,
        "Health Procrastinator" to R.drawable.healthprocrastinator,
        "Food Carefree" to R.drawable.foodcarefree
    )

    // Checks the selected persona and whether to show the pop up
    var selectedOption by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Arranging the persona buttons
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        // Row 1 for the first three persona buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            options.take(3).forEach { option ->
                Button(
                    onClick = {
                        selectedOption = option
                        showDialog = true // Show dialog with details of the selected option
                    },
                    modifier = Modifier.padding(2.dp), // Padding around button
                    contentPadding = PaddingValues(9.dp)
                ) {
                    Text( // Text displayed for each persona
                        option,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Row to display next three persona buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            options.drop(3).forEach { option ->
                Button(
                    onClick = {
                        selectedOption = option // Set selected option when clicked
                        showDialog = true // Show dialog with details of selected option
                    },
                    modifier = Modifier.padding(2.dp), // Padding around the button
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text( // Text displayed for each persona
                        option, textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // If the dialog should be displayed, then display it with the pop up
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // If user taps outside then dismiss the dialog
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        optionImages[selectedOption]?.let { imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "$selectedOption Image", // Show image of selected persona
                                modifier = Modifier.size(140.dp)
                            )
                        }
                        Text(
                            text = selectedOption, // Shows persona title
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = optionDetails[selectedOption] ?: "No details available.", // Shows persona description
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { showDialog = false }) {  // Button to close the dialog
                            Text("Dismiss")
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}