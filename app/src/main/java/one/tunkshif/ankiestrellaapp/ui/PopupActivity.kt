package one.tunkshif.ankiestrellaapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
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
import java.lang.IllegalArgumentException
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
                    if (recyclerView.visibility == View.GONE) {
                        recyclerView.visibility = View.VISIBLE
                    }
                    recyclerViewAdapter.isTranslationShort = true // TODO
                    definitionList.clear()
                    definitionList.addAll(word.definitions)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    private val definitionList = mutableListOf<Definition>()

    private val inputSearchLayout: TextInputLayout by lazy { findViewById(R.id.text_input_pop_up_search_layout) }
    private val inputSearch: TextInputEditText by lazy { findViewById(R.id.text_input_pop_up_search) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_definitions) }
    private val recyclerViewAdapter: DefinitionAdapter by lazy { recyclerView.adapter as DefinitionAdapter }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        inputSearchLayout.setEndIconOnClickListener {

            TODO()

        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PopupActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = DefinitionAdapter(this@PopupActivity, definitionList)
        }

    }
}