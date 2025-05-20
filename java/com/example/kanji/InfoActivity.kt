package com.example.kanji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kanji.data.KanjiEntry
import com.example.kanji.ui.InfoScreen
import com.example.kanji.ui.theme.KanjiTheme

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val kanji = intent.getStringExtra("kanji") ?: ""
        val korean = intent.getStringExtra("korean") ?: ""
        val ondoku = intent.getStringExtra("ondoku") ?: ""
        val kundoku = intent.getStringExtra("kundoku") ?: ""
        val song = intent.getStringExtra("song") ?: ""

        val entry = KanjiEntry(
            korean = korean,
            kanji = kanji,
            ondoku = ondoku,
            kundoku = kundoku,
            song = song
        )

        val prefs = getSharedPreferences("kanji_prefs", MODE_PRIVATE)
        val saved = prefs.getStringSet("memorized_kanji", emptySet()) ?: emptySet()
        var isMemorized = saved.contains(entry.kanji)

        setContent {
            KanjiTheme {
                InfoScreen(
                    kanjiEntry = entry,
                    onBack = { finish() },
                    onAddWord = { /* TODO */ },
                    isMemorized = isMemorized,
                    onToggleMemorized = {
                        val updated = saved.toMutableSet()
                        if (isMemorized) updated.remove(entry.kanji) else updated.add(entry.kanji)
                        isMemorized = !isMemorized
                        prefs.edit().putStringSet("memorized_kanji", updated).apply()
                    }
                )
            }
        }
    }
}