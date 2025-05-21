package com.example.a3_yangtang33840180.genAI

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a3_yangtang33840180.genAI.Message
import com.example.a3_yangtang33840180.genAI.MessageViewModel
import com.example.a3_yangtang33840180.R
import com.example.a3_yangtang33840180.ui.theme.A3_YangTang33840180Theme

@Composable
fun GenAIScreen(
    genAiViewModel: GenAIViewModel = viewModel()
) {

    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by genAiViewModel.uiState.collectAsState()
    LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier.padding(all = 16.dp)
        ) {

            Button( // Generate a AI message button
                onClick = {
                    genAiViewModel.sendPrompt("Generate a short encouraging message to help someone improve their fruit intake.")
                }//
            ){ // Button Content
                Image( // AI Chat logo
                    painter = painterResource(id = R.drawable.message),
                    contentDescription = "Messages",
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(15.dp)) // Add space between content

                Text("Motivational Message (AI)") // Button text
            }
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(132.dp)
            )
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .height(100.dp)
                    .verticalScroll(scrollState)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ){
            //ShowAllTips()
        }
    }
}

//@Composable
//fun ShowAllTips() {
//
//    // State to control the visibility of the AlertDialog.
//    var showDialog by remember { mutableStateOf(false) }
//
//    // Button that, when clicked, sets the 'showDialog'
//    // state to true, which opens the dialog.
//    // the dialog is hidden initially
//    Button(
//        onClick = {
//            showDialog = true
//        }) {
//        Image( // AI Chat logo
//            painter = painterResource(id = R.drawable.message),
//            contentDescription = "Messages",
//            modifier = Modifier.size(25.dp)
//        )
//
//        Spacer(modifier = Modifier.width(15.dp)) // Add space between content
//
//        Text("Show All Tips") // Button text
//    }
//    if (showDialog) {
//        AlertDialog(
//            //switch the visibility of the dialog to false when the user dismisses it
//            onDismissRequest = { showDialog = false },
//            title = { Text("Enter Your Name") },
//            text = {
//                Column {
//                    //#TODO Insert Database data
//                    val myViewModel: MessageViewModel = ViewModelProvider(
//                        this, MessageViewModel.MessageViewModelFactory(this@ShowAllTips)
//                    )[MessageViewModel::class.java]
//                    A3_YangTang33840180Theme {
//                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                            AddTheMessage(this@ShowAllTips, innerPadding,myViewModel)
//                        }
//                    }
//
//                }
//            },
//            confirmButton = {
//                Button(onClick = {
//                    showDialog = false
//                    //if the user clicks on the confirm button,
//                    //call the callback function 'onConfirm' with the entered text
//                    //the implementation of the callback function is in the onCreate function
//                }) {
//                    Text("Done")
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun AddTheMessage(context: Context, paddingValues: PaddingValues, viewModel: MessageViewModel) { //AddTennisPlayer
//    var messageContent by remember { mutableStateOf(TextFieldValue("")) }
//    val listOfMessages by viewModel.allMessages.collectAsState(initial = emptyList())
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(paddingValues)
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        MessageFormSection(
//            messageContent = messageContent,
//            onContentChange = { messageContent = it }
//        )
//
//        ActionButtons(
//            onAdd = {
//                viewModel.insertMessage(
//                    Message(
//                        content = messageContent.text
//                    )
//                )
//            },
//            onDeleteAll = {
//                viewModel.deleteAllMessages()
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//        HorizontalDivider(thickness = 4.dp)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        MessageList(messages = listOfMessages, context = context, viewModel = viewModel)
//    }
//}
//
//@Composable
//fun MessageFormSection(
//    messageContent: TextFieldValue,
//    onContentChange: (TextFieldValue) -> Unit
//){
//    OutlinedTextField(
//        value = messageContent,
//        onValueChange = onContentChange,
//        label = { Text("Message Content") },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 8.dp)
//    )
//}

//@Composable
//fun ActionButtons(onAdd: () -> Unit, onDeleteAll: () -> Unit) {
//    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//        // Button 1: Add
//        Button(onClick = onAdd){
//            Text("Add Message")
//        }
//        // Button 2: Delete All
//        Button(onClick = onDeleteAll,
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Red))
//        {
//            Text("Delete All Messages")
//        }
//    }
//
//}
//
//@Composable
//fun MessageList(messages: List<Message>, context: Context, viewModel: MessageViewModel){
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth()
//    ){
//        items(messages) { message ->
//            MessageCard(message = message, context = context, onDelete = {
//                viewModel.deleteMessageById(message.id)
//            })
//        }
//    }
//}

//@Composable
//fun MessageCard(message: Message, context: Context, onDelete: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .height(100.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp))
//    {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        )
//        {
//            Column(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .weight(1f)
//            ){
//                Text(text = "Name: ${message.theMessage}", fontWeight = FontWeight.Bold)
//            }
//
//            Column{
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
//                }
//            }
//        }
//    }
//}