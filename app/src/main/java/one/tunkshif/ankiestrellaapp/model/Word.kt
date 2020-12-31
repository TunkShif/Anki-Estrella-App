package one.tunkshif.ankiestrellaapp.model

data class Word(
    val word: String,
    val audio: String? = null,
    val phonetic: String? = null,
    val definitions: MutableList<Definition>
)

data class Definition(
    val partOfSpeech: String? = null,
    val translation: String,
    val sentence: String? = null
) {
    fun addToList(list: MutableList<Definition>) {
        list.add(this)
    }
}