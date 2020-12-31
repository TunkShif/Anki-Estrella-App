package one.tunkshif.ankiestrellaapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.api.AnkiDroidHelper
import one.tunkshif.ankiestrellaapp.model.Source
import one.tunkshif.ankiestrellaapp.model.Sources
import one.tunkshif.ankiestrellaapp.ui.adapter.DefinitionAdapter
import one.tunkshif.ankiestrellaapp.ui.adapter.FieldAdapter

class ProfileEditActivity : AppCompatActivity() {

    private val toolbar: MaterialToolbar by lazy { findViewById(R.id.toolbar_profile) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_fields) }
    private val inputFieldSource: AutoCompleteTextView by lazy { findViewById(R.id.text_input_profile_source) }
    private val inputFieldDeck: AutoCompleteTextView by lazy { findViewById(R.id.text_input_profile_deck) }
    private val inputFieldModel: AutoCompleteTextView by lazy { findViewById(R.id.text_input_profile_model) }
    private val recyclerViewAdapter: FieldAdapter by lazy { recyclerView.adapter as FieldAdapter }

    private val deckList = AnkiDroidHelper.ankiApi.deckList
    private val modelList = AnkiDroidHelper.ankiApi.modelList
    private val fieldList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        updateSourceListView()
        updateDeckListView()
        updateModelListView()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfileEditActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = FieldAdapter(this@ProfileEditActivity, fieldList)
        }

        inputFieldSource.setOnItemClickListener { parent, _, position, _ ->
            Log.d("ANKI-ES", "Update available fields")
            val source = Sources.sourceList[parent.getItemAtPosition(position)]
            recyclerViewAdapter.fieldAvailableList = source?.fieldsAvailableList ?: listOf()
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        inputFieldModel.setOnItemClickListener { parent, _, position, _ ->
            val modelName = parent.getItemAtPosition(position) as String
            Log.d("ANKI-ES", "Get model name: $modelName")
            val modelId = AnkiDroidHelper.findModelIdByName(modelName, 1)
            updateFieldList(AnkiDroidHelper.ankiApi.getFieldList(modelId!!).toMutableList())
        }

    }

    private fun updateSourceListView() {
        val sourceNameList = Sources.sourceList.values.toList().map { it.name }
        inputFieldSource.setAdapter(ArrayAdapter(this, R.layout.item_dropdown_list, sourceNameList))
    }

    private fun updateDeckListView() {
        val deckNameList = deckList.values.toList().sorted()
        Log.d("ANKI-ES", deckList.toString())
        inputFieldDeck.setAdapter(ArrayAdapter(this, R.layout.item_dropdown_list, deckNameList))
    }

    private fun updateModelListView() {
        val modelNameList = modelList.values.toList().sorted()
        Log.d("ANKI-ES", modelList.toString())
        inputFieldModel.setAdapter(ArrayAdapter(this, R.layout.item_dropdown_list, modelNameList))
    }

    private fun updateFieldList(newList: List<String>) {
        fieldList.clear()
        fieldList.addAll(newList)
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}