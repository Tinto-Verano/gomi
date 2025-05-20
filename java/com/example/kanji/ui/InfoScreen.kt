package com.example.kanji.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kanji.data.KanjiEntry
import com.example.kanji.ui.theme.KanjiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    kanjiEntry: KanjiEntry,
    onBack: () -> Unit,
    onAddWord: () -> Unit,
    isMemorized: Boolean,
    onToggleMemorized: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("한자 정보") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    Checkbox(checked = isMemorized, onCheckedChange = { onToggleMemorized() })
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 네모 테두리 안의 한자 + 한국어 뜻
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(kanjiEntry.kanji, fontSize = 96.sp)
                    Text(kanjiEntry.korean, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Spacer(modifier = Modifier.height(8.dp))
            Text("훈독: ${kanjiEntry.kundoku}")
            Text("음독: ${kanjiEntry.ondoku}")

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Spacer(modifier = Modifier.height(8.dp))
            Text("예시", fontWeight = FontWeight.Bold)

            kanjiEntry.song.split("、", "\n").forEach { example ->
                Text(text = example)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            // "단어" 텍스트 + +버튼 한 줄에 배치
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("단어", fontWeight = FontWeight.Bold)
                Button(onClick = onAddWord) {
                    Text("+ 단어 추가")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            WordCard(
                word = "木造",
                reading = "もくぞう",
                meaning = "목조",
                example = "木造の駅のストーブ"
            )
        }
    }
}


@Composable
fun WordCard(word: String, reading: String, meaning: String, example: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("$word（$reading）", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(meaning)
            Text(example, fontSize = 14.sp, color = Color.Gray)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InfoScreenPreview() {
    val testKanji = KanjiEntry(
        korean = "나무 목",
        kanji = "木",
        ondoku = "もく、ぼく",
        kundoku = "き、こ",
        song = "47の素敵な街へ\n僕が死のうと思ったのは\nPieces"
    )

    KanjiTheme {
        InfoScreen(
            kanjiEntry = testKanji,
            onBack = {},
            onAddWord = {},
            isMemorized = false,
            onToggleMemorized = {}
        )
    }
}