package com.example.kanji.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanji.data.KanjiEntry
import com.example.kanji.data.loadKanjiFromCsv
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KanjiViewModel(application: Application) : AndroidViewModel(application) {

    private val _kanjiList = MutableStateFlow<List<KanjiEntry>>(emptyList())
    val kanjiList: StateFlow<List<KanjiEntry>> = _kanjiList

    private val _memorizedKanji = MutableStateFlow<Set<String>>(emptySet())
    val memorizedKanji: StateFlow<Set<String>> = _memorizedKanji

    private val prefs = application.getSharedPreferences("kanji_prefs", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch {
            val saved = prefs.getStringSet("memorized_kanji", emptySet()) ?: emptySet()
            val loaded = loadKanjiFromCsv(application)
            _kanjiList.value = loaded

            val fromSong = loaded.filter { it.song.isNotBlank() }.map { it.kanji }.toSet()
            _memorizedKanji.value = saved union fromSong
        }
    }

    fun toggleMemorized(kanji: String) {
        val current = _memorizedKanji.value.toMutableSet()
        if (current.contains(kanji)) current.remove(kanji) else current.add(kanji)
        _memorizedKanji.value = current
        prefs.edit().putStringSet("memorized_kanji", current).apply()
    }

    fun isMemorized(kanji: String): Boolean {
        return _memorizedKanji.value.contains(kanji)
    }
}
