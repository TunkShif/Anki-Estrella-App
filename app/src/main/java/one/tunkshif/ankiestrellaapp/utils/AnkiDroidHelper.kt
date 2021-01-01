package one.tunkshif.ankiestrellaapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ichi2.anki.api.AddContentApi
import com.ichi2.anki.api.NoteInfo
import one.tunkshif.ankiestrellaapp.AnkiEstrella
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList


object AnkiDroidHelper {

    private const val ANKI_PACKAGE_NAME = "com.ichi2.anki"
    private const val ANKI_DECK_REF_DB = "com.ichi2.anki.api.decks"
    private const val ANKI_MODEL_REF_DB = "com.ichi2.anki.api.models"

    private val context = AnkiEstrella.context

    val ankiApi = AddContentApi(context)

    fun isAnkiDroidPermissionGranted() = ContextCompat.checkSelfPermission(
        context,
        AddContentApi.READ_WRITE_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    fun isAnkiDroidApiAvailable() = AddContentApi.getAnkiDroidPackageName(context) != null;

    fun requestPermission(callbackActivity: Activity, callbackCode: Int) =
        ActivityCompat.requestPermissions(
            callbackActivity,
            arrayOf(AddContentApi.READ_WRITE_PERMISSION),
            callbackCode
        );

    fun isAnkiDroidRunning() = ankiApi.deckList != null

    fun startAnkiDroid(): Boolean {
        val intent =
            context.packageManager.getLaunchIntentForPackage(ANKI_PACKAGE_NAME) ?: return false
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        context.startActivity(intent)
        return true
    }

    fun storeDeckReference(deckName: String?, deckId: Long) {
        val deckDb = context.getSharedPreferences(ANKI_DECK_REF_DB, Context.MODE_PRIVATE)
        deckDb.edit().putLong(deckName, deckId).apply()
    }

    fun storeModelReference(modelName: String?, modelId: Long) {
        val modelDb = context.getSharedPreferences(ANKI_MODEL_REF_DB, Context.MODE_PRIVATE)
        modelDb.edit().putLong(modelName, modelId).apply()
    }

    fun removeDuplicates(
        fields: LinkedList<Array<String>>,
        tags: LinkedList<Set<String>>,
        modelId: Long
    ) {
        // Build a list of the duplicate keys (first fields) and find all notes that have a match with each key
        val keys: MutableList<String> = ArrayList(fields.size)
        fields.forEach {
            keys.add(it[0])
        }

        val duplicateNotes: SparseArray<List<NoteInfo>> = ankiApi.findDuplicateNotes(modelId, keys)
        // Do some sanity checks
        when {
            tags.size != fields.size -> throw IllegalStateException("List of tags must be the same length as the list of fields")
            duplicateNotes.size() == 0 || fields.size == 0 || tags.size == 0 -> return
            duplicateNotes.keyAt(duplicateNotes.size() - 1) >= fields.size -> throw IllegalStateException(
                "The array of duplicates goes outside the bounds of the original lists"
            )
        }

        val fieldIterator = fields.listIterator()
        val tagIterator = tags.listIterator()
        var listIndex = -1

        for (i in 0 until duplicateNotes.size()) {
            val duplicateIndex = duplicateNotes.keyAt(i)
            while (listIndex < duplicateIndex) {
                fieldIterator.next()
                tagIterator.next()
                listIndex++
            }
            fieldIterator.remove()
            tagIterator.remove()
        }

    }

    fun findModelIdByName(modelName: String, numFields: Int): Long? {
        val modelDb = context.getSharedPreferences(ANKI_MODEL_REF_DB, Context.MODE_PRIVATE)
        val prefModelId = modelDb.getLong(modelName, -1L)
        if (prefModelId != -1L && ankiApi.getModelName(prefModelId) != null &&
            ankiApi.getFieldList(prefModelId).size >= numFields) {
            return prefModelId
        }
        val modelList = ankiApi.getModelList(numFields)
        for (entry in modelList.entries) {
            if (entry.value == modelName) {
                return entry.key
            }
        }
        return null
    }


    fun findDeckIdByName(deckName: String): Long? {
        val deckDb = context.getSharedPreferences(ANKI_DECK_REF_DB, Context.MODE_PRIVATE)
        var deckId = getDeckId(deckName)
        if (deckId != null) {
            return deckId
        } else {
            deckId = deckDb.getLong(deckName, -1)
            if (deckId != -1L && ankiApi.getDeckName(deckId) != null) {
                return deckId
            }
        }
        return null
    }

    fun getDeckId(deckName: String): Long? {
        val deckList = ankiApi.deckList
        for (entry in deckList.entries) {
            if (entry.value == deckName) {
                return entry.key
            }
        }
        return null
    }

}
