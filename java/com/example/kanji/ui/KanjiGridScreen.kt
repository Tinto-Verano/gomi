package com.example.kanji.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kanji.data.KanjiEntry
import com.example.kanji.viewmodel.KanjiViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun KanjiGridScreen(
    kanjiList: List<KanjiEntry>,
    onKanjiClick: (KanjiEntry) -> Unit,
    viewModel: KanjiViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val memorizedSet by viewModel.memorizedKanji.collectAsState()
    val lastIndex by viewModel.lastGridIndex.collectAsState()
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    // 복원 시 초기 스크롤 위치로 이동
    LaunchedEffect(kanjiList) {
        if (kanjiList.isNotEmpty()) {
            gridState.scrollToItem(lastIndex)
        }
    }

    // 현재 위치 저장
    LaunchedEffect(gridState.firstVisibleItemIndex) {
        viewModel.saveScrollPosition(gridState.firstVisibleItemIndex)
    }

    val filteredList = remember(searchQuery.text, kanjiList) {
        if (searchQuery.text.isBlank()) kanjiList
        else kanjiList.filter {
            it.kanji.contains(searchQuery.text) || it.korean.contains(searchQuery.text)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("한자 검색") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredList) { entry ->
                val isMemorized = memorizedSet.contains(entry.kanji)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .combinedClickable(
                            onClick = { onKanjiClick(entry) },
                            onLongClick = { viewModel.toggleMemorized(entry.kanji) }
                        ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    color = if (isMemorized) Color(0xFFA5D6A7) else Color.White
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(text = entry.kanji, fontSize = 36.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KanjiGridPreview() {
    val sampleList = List(9) {
        KanjiEntry(
            korean = "예시",
            kanji = listOf("日", "月", "山", "川", "空", "天", "花", "木", "人")[it % 9],
            ondoku = "にち",
            kundoku = "ひ",
            song = "예시 문장"
        )
    }
    KanjiGridScreen(kanjiList = sampleList, onKanjiClick = {})
}