package com.example.a3_yangtang33840180

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.a3_yangtang33840180.data.foodIntake.FoodIntakeRepository
import com.example.a3_yangtang33840180.data.foodIntake.FoodIntakeViewModel
import com.example.a3_yangtang33840180.data.foodIntake.FoodIntakeViewModelFactory
import com.example.a3_yangtang33840180.data.patients.PatientDatabase
import java.util.Calendar

class QuestionnairePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = intent.getIntExtra("userIdInt", -1)
        if (userId == -1) {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val dao = PatientDatabase.getDatabase(application).foodIntakeDao()
        val repository = FoodIntakeRepository(dao)
        val factory = FoodIntakeViewModelFactory(userId, repository)
        val viewModel = ViewModelProvider(this, factory)[FoodIntakeViewModel::class.java]

        setContent {
            MaterialTheme {
                QuestionnaireScreen(viewModel, userId)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(viewModel: FoodIntakeViewModel, userId: Int) {
    val context = LocalContext.current
    val foodIntake by viewModel.foodIntake.collectAsState()

    val allFoods = listOf(
        "Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds"
    )
    val allPersonas = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )
    val personaDescriptions = mapOf(
        "Health Devotee" to "You eat clean and consistent.",
        "Mindful Eater" to "You're aware of how food affects your body.",
        "Wellness Striver" to "You're always working on your diet.",
        "Balance Seeker" to "You strive for nutritional balance.",
        "Health Procrastinator" to "You delay healthy habits.",
        "Food Carefree" to "You eat what you want, when you want."
    )

    Spacer(modifier = Modifier.height(50.dp))

    foodIntake?.let { intake ->

        val selectedFoods = remember(intake.selectedFoods) { intake.selectedFoods.toMutableSet() }
        var personaDropdown by remember { mutableStateOf(intake.selectedPersona ?: "") }
        var mealTime by remember { mutableStateOf(intake.biggestMealTime ?: "00:00") }
        var sleepTime by remember { mutableStateOf(intake.sleepTime ?: "00:00") }
        var wakeTime by remember { mutableStateOf(intake.wakeTime ?: "00:00") }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            TopNavBar()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Tick all the food categories you can eat", fontWeight = FontWeight.Bold)

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    userScrollEnabled = false
                ) {
                    items(allFoods) { item ->
                        val checked = selectedFoods.contains(item)
                        FilterChip(
                            selected = checked,
                            onClick = {
                                if (checked) selectedFoods.remove(item) else selectedFoods.add(item)
                                viewModel.updateFoodCategories(selectedFoods.toList())
                            },
                            label = { Text(item) }
                        )
                    }
                }

                Text("Your Persona", fontWeight = FontWeight.Bold)
                Text("People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!")

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    userScrollEnabled = false
                ) {
                    items(allPersonas) { persona ->
                        Button(
                            onClick = {
                                personaDescriptions[persona]?.let {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                }
                                viewModel.updatePersona(persona)
                                personaDropdown = persona
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                        ) {
                            Text(persona, color = Color.White)
                        }
                    }
                }

                Text("Which persona best fits you?", fontWeight = FontWeight.Bold)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = personaDropdown,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select option") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        allPersonas.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    viewModel.updatePersona(label)
                                    personaDropdown = label
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select the times that best fits your habits", fontWeight = FontWeight.Bold)

                TimeInput(label = "Biggest Meal Time", time = mealTime) {
                    mealTime = it
                    viewModel.updateBiggestMealTime(it)
                }

                TimeInput(label = "Sleep Time", time = sleepTime) {
                    sleepTime = it
                    viewModel.updateSleepTime(it)
                }

                TimeInput(label = "Wake Time", time = wakeTime) {
                    wakeTime = it
                    viewModel.updateWakeTime(it)
                }

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    modifier = Modifier
                            .fillMaxWidth(),
                onClick = {
                    val intent = Intent(context, ActivityPage::class.java).apply {
                        putExtra("userIdInt", userId)
                    }
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }){
                    Text("Save", color = Color.White)
                }

                // Add some bottom padding to ensure the save button is not cut off
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar() {
    val context = LocalContext.current
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "Food Intake Questionnaire",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}

@Composable
fun TimeInput(label: String, time: String, onTimeChange: (String) -> Unit) {
    val context = LocalContext.current
    OutlinedTextField(
        value = time,
        onValueChange = {},
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = {
                val cal = Calendar.getInstance()
                val timeParts = time.split(":")
                val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: cal.get(Calendar.HOUR_OF_DAY)
                val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: cal.get(Calendar.MINUTE)

                TimePickerDialog(
                    context,
                    { _, h, m -> onTimeChange(String.format("%02d:%02d", h, m)) },
                    hour, minute, true
                ).show()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Clock Image",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
}