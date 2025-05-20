package com.example.kanji

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.kanji.ui.KanjiGridScreen
import com.example.kanji.viewmodel.KanjiViewModel

class KanjiActivity : ComponentActivity() {
    private val viewModel: KanjiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val kanjiList = viewModel.kanjiList.collectAsState()

            KanjiGridScreen(
                kanjiList = kanjiList.value,
                onKanjiClick = { clicked ->
                    val intent = Intent(this, InfoActivity::class.java).apply {
                        putExtra("kanji", clicked.kanji)
                        putExtra("korean", clicked.korean)
                        putExtra("ondoku", clicked.ondoku)
                        putExtra("kundoku", clicked.kundoku)
                        putExtra("song", clicked.song)
                    }
                    startActivity(intent)
                }
            )
        }
    }
}
