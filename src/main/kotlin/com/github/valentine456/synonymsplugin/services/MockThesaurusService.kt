package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.Service
import java.util.*

@Service(Service.Level.PROJECT)
class MockThesaurusService() : ThesaurusService {

    val thesaurus: TreeMap<String, ArrayList<String>> = TreeMap<String, ArrayList<String>>()
    override var nextService: ThesaurusService? = null

    init {
        thesaurus["get"] = arrayListOf("receive", "fetch", "obtain", "read", "retrieve");
        thesaurus["send"] = arrayListOf("transmit", "transfer", "deliver")
        thesaurus["clean"] = arrayListOf("delete", "clear")
    }

    override fun getSynonyms(identifier: String): ArrayList<String> {
        return thesaurus.getOrDefault(identifier.lowercase(), arrayListOf());
    }
}