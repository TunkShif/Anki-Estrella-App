package one.tunkshif.ankiestrellaapp.model.sources

import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Source
import one.tunkshif.ankiestrellaapp.utils.getStringFromResources

object Basic : Source() {
    override val name: String = "Basic"
    override val displayName: String = getStringFromResources(R.string.source_name_basic)
    override val description: String = getStringFromResources(R.string.source_name_basic_desc)
    override val fieldsAvailableList: List<String>
        get() = listOf(
            getStringFromResources(R.string.field_available_front),
            getStringFromResources(R.string.field_available_back)
        )

    override fun mapValueToFieldsList(): Map<String, String> {
        TODO("Not yet implemented")
    }
}