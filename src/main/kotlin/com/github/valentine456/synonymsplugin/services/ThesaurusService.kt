package com.github.valentine456.synonymsplugin.services

interface ThesaurusService {
    public fun getSynonyms (identifier: String ): ArrayList<String>
    public var nextService: ThesaurusService?
}