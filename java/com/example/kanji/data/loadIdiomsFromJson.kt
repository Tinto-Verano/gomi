package com.example.kanji.data

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

fun loadIdiomsFromJson(context: Context): Map<String, String> {
    val idiomMap = mutableMapOf<String, String>()
    val inputStream = context.assets.open("idioms.json")
    val reader = BufferedReader(InputStreamReader(inputStream))
    val jsonString = reader.readText()
    reader.close()

    val jsonObject = JSONObject(jsonString)
    val keys = jsonObject.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        val value = jsonObject.getString(key)
        idiomMap[key] = value
    }
    return idiomMap
}
