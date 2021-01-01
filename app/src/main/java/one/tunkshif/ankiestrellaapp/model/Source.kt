package one.tunkshif.ankiestrellaapp.model

import one.tunkshif.ankiestrellaapp.model.sources.Basic
import one.tunkshif.ankiestrellaapp.model.sources.SpanishDict

abstract class Source {
    abstract val name: String
    abstract val displayName: String
    abstract val description: String
    abstract val fieldsAvailableList: List<String>

    abstract fun mapValueToFieldsList(): Map<String, String>
}

object Sources {

    val sourceList: MutableMap<String, Source>
        get() = mutableMapOf(Basic.name to Basic, SpanishDict.name to SpanishDict)

}

abstract class DictSource : Source() {
    abstract val apiUrl: String
    abstract val isPhraseAvailable: Boolean

    abstract fun wordQuery(query: String): Word?

}