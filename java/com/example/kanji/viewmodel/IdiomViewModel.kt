package com.example.kanji.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanji.data.loadIdiomsFromJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class IdiomViewModel(application: Application) : AndroidViewModel(application) {

    private val _idiomMap = mutableMapOf<String, String>()
    private val _currentIdiom = MutableStateFlow("")
    val currentIdiom: StateFlow<String> = _currentIdiom

    private var currentAnswer = ""

    private val _feedback = MutableStateFlow("")
    val feedback: StateFlow<String> = _feedback

    init {
        viewModelScope.launch {
            _idiomMap.putAll(loadIdiomsFromJson(application))
            nextQuestion()
        }
    }

    fun checkAnswer(userInput: String) {
        if (userInput.trim() == currentAnswer) {
            _feedback.value = "정답입니다!"
            nextQuestion()
        } else {
            _feedback.value = "틀렸습니다. 정답: $currentAnswer"
        }
    }

    fun nextQuestion() {
        if (_idiomMap.isNotEmpty()) {
            val randomEntry = _idiomMap.entries.toList().random(Random)
            _currentIdiom.value = randomEntry.key
            currentAnswer = randomEntry.value
            _feedback.value = ""
        }
    }
}
