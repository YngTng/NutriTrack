package com.example.a3_yangtang33840180

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme

// Main activity that starts when app is opened
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Makes the app use the whole screen
        setContent {
            A3_YangTang33840180Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen( // Calling the NutriTrack Screen function
                        modifier = Modifier.padding(innerPadding) // Add padding in my screen
                    )
                }
            }
        }
    }
}

// Shows the NutriTrack welcome page
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current // This is to get the current context for navigation later on

    Box(
        modifier = Modifier
            .fillMaxSize() // Makes the box take up the whole screen
    )
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Set background to app's theme colour
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Adds padding around the elements
            horizontalAlignment = Alignment.CenterHorizontally, // Centers horizontally
            verticalArrangement = Arrangement.Center // Centers vertically
        ) {
            Text(
                text = "NutriTrack", // App title
                style = androidx.compose.ui.text.TextStyle(fontSize = 45.sp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp)) // Add space below title

            androidx.compose.foundation.Image( // App logo
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) //Add space below logo

            Text(
                text = "This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. \n Use this app at your own risk.\n" + "If you’d like to an Accredited Practicing Dietitian (APD), \n please visit the Monash Nutrition/Dietetics Clinic \n (discounted rates for students):\n" + "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    context.startActivity(Intent(context, LoginPage()::class.java))
                }
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Yang Tang (33840180)",
                textAlign = TextAlign.Center)
        }
    }
}