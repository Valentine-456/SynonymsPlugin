package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.Service
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service(Service.Level.PROJECT)
class BigHugeThesaurusService : ThesaurusService {

    override var nextService: ThesaurusService? = null
    private val key = "8a5dd3a4727152433113a6d5da23606c"
    private val json = Json { ignoreUnknownKeys = true }

    override fun getSynonyms(identifier: String): ArrayList<String> {
        try {
            val client = HttpClient.newBuilder().build()
            val request =
                HttpRequest.newBuilder()
                    .uri(URI.create("https://words.bighugelabs.com/api/2/$key/$identifier/json"))
                    .GET()
                    .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() in 200..303) {
                println(response.body())
                val wordInfo = json.decodeFromString(BigHugeThesaurusService.WordInfo.serializer(), response.body())
                val (noun, verb) = wordInfo

                return if ((noun != null) and (verb != null)) {
                    if (noun!!.syn.size >= verb!!.syn.size) noun.syn
                    else verb.syn
                } else if ((noun != null) or (verb != null)) {
                    noun?.syn ?: verb!!.syn
                } else throw Exception()
            } else throw Exception()

        } catch (e: Exception) {
            nextService?.let {
                return it.getSynonyms(identifier)
            }
        }
        return arrayListOf()
    }

    @Serializable
    data class WordInfo(
        val noun: WordType?, val verb: WordType?
    )

    @Serializable
    data class WordType(
        val syn: ArrayList<String>
    )
}