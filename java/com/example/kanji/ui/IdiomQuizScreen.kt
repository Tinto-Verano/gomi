package com.example.kanji.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kanji.viewmodel.IdiomViewModel

@Composable
fun IdiomQuizScreen(viewModel: IdiomViewModel = viewModel()) {
    val currentIdiom by viewModel.currentIdiom.collectAsState()
    val feedback by viewModel.feedback.collectAsState()

    var userInput by remember { mutableStateOf("") }
    var showInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentIdiom,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { showInput = true }
        )

        if (showInput) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("음독을 입력하세요") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                viewModel.checkAnswer(userInput)
                userInput = ""
            }) {
                Text("정답 확인")
            }

            if (feedback.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = feedback, color = Color.DarkGray)
            }
        }
    }
}
