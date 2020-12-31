package one.tunkshif.ankiestrellaapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Definition

class DefinitionAdapter(
    private val context: Context,
    private val definitionList: List<Definition>
) : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    var isTranslationShort: Boolean = false

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chipWordClass: Chip = view.findViewById(R.id.chip_word_class)
        val textLong: TextView = view.findViewById(R.id.text_long)
        val textShort: TextView = view.findViewById(R.id.text_short)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_list_definition, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definition = definitionList[position]

        if (definition.partOfSpeech != null) {
            holder.chipWordClass.apply {
                visibility = View.VISIBLE
                text = definition.partOfSpeech
            }
        }

        holder.textShort.apply {
            visibility = if (isTranslationShort) View.VISIBLE else View.GONE
            text = if (isTranslationShort) definition.translation else ""
        }

        holder.textLong.apply {
            visibility = View.VISIBLE
            text = if (isTranslationShort) definition.sentence else definition.translation
        }

    }

    override fun getItemCount() = definitionList.size

}