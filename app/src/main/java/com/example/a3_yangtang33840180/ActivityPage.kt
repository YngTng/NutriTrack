package com.example.a3_yangtang33840180

import android.os.Bundle
import android.util.Log
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.a3_yangtang33840180.data.fruityVice.FruitViewModel
import com.example.a3_yangtang33840180.data.genAI.GenAIViewModel
import com.example.a3_yangtang33840180.data.genAI.UiState
import com.example.a3_yangtang33840180.data.genAI.Message
import com.example.a3_yangtang33840180.data.genAI.MessageViewModel
import com.example.a3_yangtang33840180.data.patients.Patient
import com.example.a3_yangtang33840180.data.patients.PatientDAO
import com.example.a3_yangtang33840180.data.patients.PatientDatabase
import com.example.a3_yangtang33840180.data.patients.PatientRepository
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme

class ActivityPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A3_YangTang33840180Theme {
                val navController: NavHostController = rememberNavController() // Keeps track of current screen
                val selectedItemState = remember { mutableIntStateOf(0) }

                val userId = intent.getIntExtra("USER_ID", -1)
                val context = LocalContext.current
                val db = remember { PatientDatabase.getDatabase(context) }
                val patientDao = remember { db.patientDao() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { // Defines the bottom navigation bar
                        BottomBar(navController = navController, selectedItemState = selectedItemState)
                    }
                ){ innerPadding -> // Makes sure the layout isn't hidden under bottom bar
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MyNavHost(navController, selectedItemState, userId = userId, patientDao = patientDao)
                    }
                }
            }
        }
    }
}

@Composable
fun MyNavHost(navController: NavHostController, selectedItemState: MutableState<Int>, userId: Int, patientDao: PatientDAO) {
    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home") {
            HomePage(userId = userId)
        }
        // Define the composable for the "insights" route
        composable("Insights") {
            InsightsPage(
                navController = navController,
                selectedItemState = selectedItemState,
                userId = userId,
                patientDao = patientDao   // Pass DAO here
            )
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


@Composable
fun HomePage(userId: Int) {
    val context = LocalContext.current
    val repository = PatientRepository.getInstance(context)

    var patient by remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(userId) {
        patient = repository.getPatientById(userId)
    }

    val name = patient?.name ?: "Unknown"
    val gender = patient?.sex ?: "Unknown"
    val totalScore = when (gender) {
        "Male" -> patient?.heifaTotalScoreMale ?: 0.0
        "Female" -> patient?.heifaTotalScoreFemale ?: 0.0
        else -> 0.0
    }

    Log.d("DEBUG", "User ID: $userId, Name: $name, Gender: ${patient?.sex}, Total Score: $totalScore")

    if (patient == null) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Hello,",
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = name,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    context.startActivity(Intent(context, QuestionnairePage()::class.java))
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

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.plate),
            contentDescription = "Plate",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "My Score",
            style = TextStyle(fontSize = 18.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically) {

            Image(
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
                    text = "$totalScore / 100",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "What is the Food Quality Score?",
            style = TextStyle(fontSize = 18.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\nThis personalised measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            style = TextStyle(fontSize = 15.sp)
        )
    }
}

// Function for the Insights page to display the progress bars
@Composable
fun InsightsPage(
    navController: NavHostController,
    userId: Int,
    patientDao: PatientDAO, // inject the DAO directly
    selectedItemState: MutableState<Int> = remember { mutableIntStateOf(0) }
) {

    var patient by remember { mutableStateOf<Patient?>(null) }

    // Collect patient from DAO (Room Flow)
    LaunchedEffect(userId) {
        patient = patientDao.getPatientById(userId)
    }

    // Gender check function
    fun isMalePatient(patient: Patient?): Boolean {
        return patient?.sex.equals("male", ignoreCase = true)
    }

    // Column headers and max scores (same as before)
    val columnHeaders = listOf(
        "Discretionary", "Vegetables", "Fruit", "Grains/Cereals", "Wholegrains", "Meat", "Dairy",
        "Sodium", "Alcohol", "Water", "Sugar", "Saturated fat", "Unsaturated fat"
    )

    val maxScores = listOf(10f, 10f, 10f, 5f, 5f, 10f, 10f, 10f, 5f, 5f, 10f, 5f, 5f)

    val userValues = patient?.let {
        val isMale = isMalePatient(it)

        listOf(
            if (isMale) it.discretionaryHeifaScoreMale else it.discretionaryHeifaScoreFemale,
            if (isMale) it.vegetablesHeifaScoreMale else it.vegetablesHeifaScoreFemale,
            if (isMale) it.fruitHeifaScoreMale else it.fruitHeifaScoreFemale,
            if (isMale) it.grainsAndCerealsHeifaScoreMale else it.grainsAndCerealsHeifaScoreFemale,
            if (isMale) it.wholeGrainsHeifaScoreMale else it.wholeGrainsHeifaScoreFemale,
            if (isMale) it.meatAndAlternativesHeifaScoreMale else it.meatAndAlternativesHeifaScoreFemale,
            if (isMale) it.dairyAndAlternativesHeifaScoreMale else it.dairyAndAlternativesHeifaScoreFemale,
            if (isMale) it.sodiumHeifaScoreMale else it.sodiumHeifaScoreFemale,
            if (isMale) it.alcoholHeifaScoreMale else it.alcoholHeifaScoreFemale,
            if (isMale) it.waterHeifaScoreMale else it.waterHeifaScoreFemale,
            if (isMale) it.sugarHeifaScoreMale else it.sugarHeifaScoreFemale,
            if (isMale) it.saturatedFatHeifaScoreMale else it.saturatedFatHeifaScoreFemale,
            if (isMale) it.unsaturatedFatHeifaScoreMale else it.unsaturatedFatHeifaScoreFemale,
        )
    } ?: emptyList()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Insights: Food Score",
            style = TextStyle(fontSize = 25.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        columnHeaders.forEachIndexed { index, column ->
            var progress by remember { mutableDoubleStateOf(userValues.getOrNull(index) ?: 0.0) }
            val maxProgress = maxScores.getOrNull(index) ?: 10f

            val inactiveTrack = colorResource(id = R.color.purple_200)
            val activeTrack = colorResource(id = R.color.purple_500)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = column,
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.Bold
                )

                LaunchedEffect(userValues.getOrNull(index)) {
                    progress = userValues.getOrNull(index) ?: 0.0
                }

                LinearProgressIndicator(
                    progress = (progress / maxProgress).toFloat().coerceIn(0f, 1f),
                    modifier = Modifier
                        .weight(2f)
                        .height(5.dp),
                    color = activeTrack,
                    trackColor = inactiveTrack
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    contentAlignment = Alignment.CenterEnd
                ){
                    Text(
                        text = "${"%.1f".format(progress)}/${maxProgress}",
                        style = TextStyle(fontSize = 15.sp)
                    )
                }
            }
        }

        // Total score section
        patient?.let { nonNullPatient ->
            val isMale = isMalePatient(nonNullPatient)
            TotalValues(patient = nonNullPatient, isMale = isMale)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp),
                onClick = {
                    selectedItemState.value = 2
                    navController.navigate("NutriCoach")
                }
            ) {
                Image(
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
    val context = LocalContext.current
    val messageViewModel: MessageViewModel = viewModel(
        factory = MessageViewModel.MessageViewModelFactory(context)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("NutriCoach", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ){
            SearchFruits()

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                thickness = 3.dp,
                color = Color.LightGray
            )

            GenAIScreen(messageViewModel = messageViewModel)
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
            style = TextStyle(fontSize = 25.sp),
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
                style = TextStyle(fontSize = 15.sp),
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
                style = TextStyle(fontSize = 15.sp),
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
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log Out")

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                Text("Logout")
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
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
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
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
                if (clinicianKey == "dollar-entry-apples") {
                    navController.navigate("ClinicianDashboard")
                } else {
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ){ // Button Content
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log In") // Icon for login button

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

@Composable
fun TotalValues(patient: Patient, isMale: Boolean) {
    val genderedScore = if (isMale) patient.heifaTotalScoreMale else patient.heifaTotalScoreFemale

    var progress by remember { mutableFloatStateOf(genderedScore.toFloat()) }

    val inactiveTrack = colorResource(id = R.color.purple_200)
    val activeTrack = colorResource(id = R.color.purple_500)

    Column(modifier = Modifier.padding(1.dp)) {
        Spacer(modifier = Modifier.height(45.dp))

        Text(
            text = "Total Food Quality Score",
            style = TextStyle(fontSize = 17.sp),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LaunchedEffect(genderedScore) {
                progress = genderedScore.toFloat()
            }

            LinearProgressIndicator(
                progress = (progress / 100f).coerceIn(0f, 1f),
                modifier = Modifier
                    .weight(5f)
                    .height(6.dp),
                color = activeTrack,
                trackColor = inactiveTrack
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "${"%.1f".format(progress)}/100",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
        }

        val context = LocalContext.current
        val shareScore = "My total food quality score is $progress!"

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareScore)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share with someone"))
                },
                modifier = Modifier.sizeIn(minWidth = 120.dp, minHeight = 40.dp)
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                )
                Text("Share with someone")
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

@Composable
fun GenAIScreen(
    genAiViewModel: GenAIViewModel = viewModel(),
    messageViewModel: MessageViewModel
) {
    val placeholderResult = stringResource(R.string.results_placeholder)
    val uiState by genAiViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var result by rememberSaveable { mutableStateOf(placeholderResult) }

    val generatedMsg = (uiState as? UiState.Success)?.outputText

    //Save message to MessageDatabase
    LaunchedEffect(generatedMsg) {
        generatedMsg?.let {
            messageViewModel.insertMessage(Message(theMessage = it))
            result = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Button(onClick = {
            genAiViewModel.sendPrompt("Generate a short encouraging message to help someone improve their fruit intake.")
        }) {
            Icon(
                painter = painterResource(id = R.drawable.message),
                contentDescription = "AI Message",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Motivational Message (AI)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is UiState.Error -> {
                val error = (uiState as UiState.Error).errorMessage
                result = error
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            is UiState.Success -> {
                Text(
                    text = result,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
            }

            else -> {
                Text(
                    text = placeholderResult,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ShowAllMessages()
    }
}

@Composable
fun ShowAllMessages() {
    val context = LocalContext.current
    val messageViewModel: MessageViewModel = viewModel(
        factory = MessageViewModel.MessageViewModelFactory(context)
    )
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.message),
            contentDescription = "Messages",
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text("Show All Messages")
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "All Messages",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AddMessageDialogContent(
                        viewModel = messageViewModel,
                        onCloseDialog = { showDialog = false } // Pass close action here
                    )
                }
            }
        }
    }
}

@Composable
fun AddMessageDialogContent(viewModel: MessageViewModel, onCloseDialog: () -> Unit) {
    val listOfMessages by viewModel.allMessages.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Fill remaining space
        ) {
            items(listOfMessages) { message ->
                MessageCard(
                    message = message
                )
            }
        }

        HorizontalDivider(thickness = 3.dp, color = Color.DarkGray, modifier = Modifier.padding(vertical = 18.dp))

        ActionButtons(
            onDeleteAll = {
                viewModel.deleteAllMessages()
            },
            onCloseDialog = onCloseDialog
        )
    }
}

@Composable
fun ActionButtons(onDeleteAll: () -> Unit, onCloseDialog: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()) {
        Box {
            Button(
                modifier = Modifier.sizeIn(minWidth = 20.dp, minHeight = 20.dp),
                onClick = onDeleteAll,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete All Messages")
            }
        }

        Box {
            Button(
                modifier = Modifier.sizeIn(minWidth = 50.dp, minHeight = 20.dp),
                onClick = onCloseDialog
            ){
                Text("Done")
            }
        }
    }
}

@Composable
fun MessageCard(message: Message) {
    Card(
        modifier = Modifier
            .padding(start = 1.dp, end = 1.dp, top = 4.dp, bottom = 4.dp)
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp))
    {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = message.theMessage)
        }
    }
}