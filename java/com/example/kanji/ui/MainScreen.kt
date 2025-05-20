package com.example.kanji.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kanji.viewmodel.IdiomViewModel

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: IdiomViewModel = viewModel()
) {
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
            modifier = Modifier.clickable {
                showInput = !showInput // 클릭 시 열고 닫기 토글
                userInput = ""
            }
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

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }

        MainMenuButtons(onNavigate)
    }
}

@Composable
fun MainMenuButtons(onNavigate: (String) -> Unit) {
    MainButton("한자 리스트") { onNavigate("kanji") }
    Spacer(modifier = Modifier.height(16.dp))
    MainButton("단어장") { onNavigate("vocab") }
    Spacer(modifier = Modifier.height(16.dp))
    MainButton("퀴즈") { onNavigate("quiz") }
}

@Composable
fun MainButton(text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(220.dp)
            .height(56.dp),
        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF607D8B))
    ) {
        Text(text, fontSize = 18.sp, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
