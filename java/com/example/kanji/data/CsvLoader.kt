package com.example.kanji.data

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

fun loadKanjiFromCsv(context: Context): List<KanjiEntry> {
    val kanjiList = mutableListOf<KanjiEntry>()
    val inputStream = context.assets.open("2136.csv")
    val reader = BufferedReader(InputStreamReader(inputStream))

    reader.readLine() // 첫 줄은 헤더라서 skip
    reader.forEachLine { line ->
        val tokens = line.split(",")
        if (tokens.size >= 5) {
            kanjiList.add(
                KanjiEntry(
                    korean = tokens[0].trim(),
                    kanji = tokens[1].trim(),
                    ondoku = tokens[2].trim(),
                    kundoku = tokens[3].trim(),
                    song = tokens[4].trim()
                )
            )
        }
    }

    return kanjiList
}