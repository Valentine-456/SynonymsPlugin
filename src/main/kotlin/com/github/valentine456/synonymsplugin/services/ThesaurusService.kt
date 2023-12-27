package com.github.valentine456.synonymsplugin.services

import com.intellij.openapi.components.Service
import java.util.TreeMap

@Service(Service.Level.PROJECT)
class ThesaurusService() {

    val thesaurus: TreeMap<String, ArrayList<String>> = TreeMap<String, ArrayList<String>>()
    init {
        thesaurus["get"] = arrayListOf("receive", "fetch", "obtain", "read","retrieve");
        thesaurus["send"] = arrayListOf("transmit", "transfer", "deliver")
        thesaurus["clean"] = arrayListOf("delete", "clear")
    }

    public fun getSynonyms(identifier: String ): ArrayList<String>  {
        return thesaurus.getOrDefault(identifier.lowercase(), arrayListOf("hello"));
    }
}