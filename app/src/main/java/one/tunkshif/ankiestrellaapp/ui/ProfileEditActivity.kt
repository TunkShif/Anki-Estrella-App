package one.tunkshif.ankiestrellaapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.utils.AnkiDroidHelper
import one.tunkshif.ankiestrellaapp.model.Sources
import one.tunkshif.ankiestrellaapp.ui.adapter.FieldAdapter
import one.tunkshif.ankiestrellaapp.utils.getStringFromResources
import one.tunkshif.ankiestrellaapp.utils.makeToast

class ProfileEditActivity : AppCompatActivity() {

    private val toolbar: MaterialToolbar by lazy { findViewById(R.id.toolbar_profile) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_fields) }
    private val inputFieldProfile: TextInputEditText by lazy { findViewById(R.id.text_input_profile_name) }
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

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.top_bar_menu_item_save -> {
                    if (isFieldFilled()) {
                        TODO()
                    } else {
                        makeToast(getStringFromResources(R.string.warning_text_input_fields_empty))
                    }
                    true
                }
                else -> false
            }
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

    private fun isFieldFilled(): Boolean {
        // need a better way for input field validating
        if (inputFieldProfile.text.toString() == "") {
            return false
        }
        if (inputFieldSource.text.toString() == "") {
            return false
        }
        if (inputFieldDeck.text.toString() == "") {
            return false
        }
        if (inputFieldModel.text.toString() == "") {
            return false
        }
        val fieldCount = recyclerViewAdapter.itemCount
        Log.d("ANKI-ES", "$fieldCount")
        for (i in 0 until fieldCount) {
            val textInputView: AutoCompleteTextView = recyclerView.layoutManager!!.findViewByPosition(i)!!.findViewById(R.id.text_input_field)
            if (textInputView.text.toString() == "") {
                return false
            }
        }
        return true
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