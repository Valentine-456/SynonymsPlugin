package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.Service
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service(Service.Level.PROJECT)
class ApiNinjaThesaurusService : ThesaurusService {
    private val key = "Xgvqov1N4EuFx3tRRWFSPw==Bc5tiz4JMd0wjBmS"
    private val json = Json { ignoreUnknownKeys = true }

    override fun getSynonyms(identifier: String): ArrayList<String> {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.api-ninjas.com/v1/thesaurus?word=$identifier"))
            .header("X-Api-Key", key)
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val wordData = json.decodeFromString<WordData>(WordData.serializer(), response.body())
        return wordData.synonyms ?: arrayListOf()
    }

    @Serializable
    data class WordData(
        val word: String, val synonyms: ArrayList<String>, val antonyms: ArrayList<String>
    )
}
