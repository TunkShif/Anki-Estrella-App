package one.tunkshif.ankiestrellaapp.model

class Profile(
    val name: String,
    val iconId: Int,
    val source: String,
    val deck: String,
    val model: String,
    val filedMap: MutableMap<String, String>
)
