package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.Service
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

@Service(Service.Level.PROJECT)
class FileThesaurusService : ThesaurusService {

    private val json = Json { ignoreUnknownKeys = true }
    override var nextService: ThesaurusService? = null
    private var wordMap = TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER)

    init {
        loadWordsIntoTreeMap("ThesaurusDictionary/thesaurus.json")
    }

    override fun getSynonyms(identifier: String): ArrayList<String> {
        wordMap[identifier]?.let {
            return ArrayList(it)
        } ?: nextService?.let {
            return it.getSynonyms(identifier)
        }
        return arrayListOf()
    }

    private fun loadWordsIntoTreeMap(filePath: String) {
        val fileInputStream = javaClass.classLoader.getResourceAsStream(filePath)
        if (fileInputStream == null) {
            println("Resource not found: $filePath")
            return
        }
        try {
            val jsonString = fileInputStream.bufferedReader().use { it.readText() }
            val map: Map<String, List<String>> = json.decodeFromString(jsonString)
            wordMap.putAll(map)
        } catch (e: Exception) {
            println("Error reading or parsing resource: ${e.message}")
            e.printStackTrace()
        }
    }
}
