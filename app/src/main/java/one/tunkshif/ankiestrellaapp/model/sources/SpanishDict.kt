package one.tunkshif.ankiestrellaapp.model.sources

import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Definition
import one.tunkshif.ankiestrellaapp.model.DictSource
import one.tunkshif.ankiestrellaapp.model.Word
import one.tunkshif.ankiestrellaapp.utils.getStringFromResources
import org.jsoup.Jsoup

object SpanishDict : DictSource() {
    override val name: String = "SpanishDict"
    override val displayName: String = getStringFromResources(R.string.source_name_spanish_dict)
    override val iconId: Int = R.drawable.ic_color_tag
    override val description: String = getStringFromResources(R.string.source_desc_spanish_dict)
    override val apiUrl: String = "https://www.spanishdict.com/translate/"
    override val isPhraseAvailable: Boolean = false
    override val fieldsAvailableList: List<String>
        get() = listOf(
            getStringFromResources(R.string.field_available_word),
            getStringFromResources(R.string.field_available_audio),
            getStringFromResources(R.string.field_available_part_of_speech),
            getStringFromResources(R.string.field_available_definition),
            getStringFromResources(R.string.field_available_sentence)
        )

    override fun wordQuery(query: String): Word? {
        val response = Jsoup.connect(apiUrl + query).get()
        val doc = response.select("#dictionary-neodict-es")

        if (!doc.isEmpty()) {

            val word = doc.select("span._3QFIA64h")[0].text()
            val audio = "https://api.frdic.com/api/v2/speech/speakweb?langid=es&txt=$word"
            val definitions = mutableListOf<Definition>()

            doc.select("._2TSMt8mh").forEach {
                val wordClass = it.select("._2MYNwPb3").text()
                for (group in it.select("._31pLJNT4")
                    .filter { e -> e.selectFirst("._2M9naesu") != null }) {
                    for (item in group.select("._1IN7ttrU > ._31pLJNT4")) {
                        val translation = if (item.selectFirst("._1UD6CASd") != null) {
                            group.selectFirst("._2M9naesu")
                                .text() + " " + item.selectFirst("._1UD6CASd").text()
                        } else {
                            group.selectFirst("._2M9naesu")
                                .text() + " " + item.selectFirst(".hNI9vSMp").text()
                        }
                        val sentence = item.selectFirst("._1f2Xuesa")
                            .text() + "\n" + item.selectFirst("._3WrcYAGx").text()
                        Definition(wordClass, translation, sentence).addToList(definitions)
                    }
                }
            }

            return Word(word, audio, null, definitions)
        }
        return null
//        else {
//            throw NullPointerException("Can't find the word $query in ${this.displayName}!")
//        }
    }

    override fun mapValueToFieldsList(): Map<String, String> {
        TODO("Not yet implemented")
    }
}