package com.example.kanji

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kanji.ui.MainScreen
import com.example.kanji.ui.theme.KanjiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KanjiTheme {
                MainScreen(onNavigate = { destination ->
                    when (destination) {
                        "kanji" -> {
                            val intent = Intent(this, KanjiActivity::class.java)
                            startActivity(intent)
                        }
                        "vocab" -> {
                            // 단어장 화면 연결 예정
                        }
                        "quiz" -> {
                            // 퀴즈 화면 연결 예정
                        }
                    }
                })
            }
        }
    }
}
