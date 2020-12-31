package one.tunkshif.ankiestrellaapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Definition
import one.tunkshif.ankiestrellaapp.model.Word
import one.tunkshif.ankiestrellaapp.model.sources.SpanishDict
import one.tunkshif.ankiestrellaapp.ui.adapter.DefinitionAdapter
import one.tunkshif.ankiestrellaapp.utils.makeToast
import kotlin.concurrent.thread

class PopupActivity : AppCompatActivity() {

    companion object {
        const val MSG_FETCH_WORD_FAILED = 0
        const val MSG_FETCH_WORD_SUCCESS = 1
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FETCH_WORD_SUCCESS -> {
                    val word = msg.obj as Word
                    updateDefinitionListView(word)
                }
                MSG_FETCH_WORD_FAILED -> {
                    val s = msg.obj as String
                    makeToast("Can't find the word $s in SpanishDict")
                }
            }
        }
    }

    private val definitionList = mutableListOf<Definition>()

    private val transparentExitButton: Button by lazy { findViewById(R.id.button_pop_up_transparent) }
    private val inputSearchLayout: TextInputLayout by lazy { findViewById(R.id.text_input_pop_up_search_layout) }
    private val inputSearch: TextInputEditText by lazy { findViewById(R.id.text_input_pop_up_search) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_definitions) }
    private val recyclerViewAdapter: DefinitionAdapter by lazy { recyclerView.adapter as DefinitionAdapter }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        transparentExitButton.setOnClickListener { finish() }

        inputSearchLayout.setEndIconOnClickListener { asyncWordQuery() }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PopupActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = DefinitionAdapter(this@PopupActivity, definitionList)
        }

    }

    private fun updateDefinitionListView(word: Word) {
        if (recyclerView.visibility == View.GONE) {
            recyclerView.visibility = View.VISIBLE
        }
        recyclerViewAdapter.isTranslationShort = true // TODO
        definitionList.clear()
        definitionList.addAll(word.definitions)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun asyncWordQuery() {
        thread {
            val query = inputSearch.text.toString()
            val word = SpanishDict.wordQuery(query)
            val msg = Message()
            if (word != null) {
                msg.apply {
                    what = MSG_FETCH_WORD_SUCCESS
                    obj = word
                }
            } else {
                msg.apply {
                    what = MSG_FETCH_WORD_FAILED
                    obj = query
                }
            }
            handler.sendMessage(msg)
        }
    }
}