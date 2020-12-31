package one.tunkshif.ankiestrellaapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import one.tunkshif.ankiestrellaapp.R

class FieldAdapter(private val context: Context, private val fieldList: List<String>) :
    RecyclerView.Adapter<FieldAdapter.ViewHolder>() {

    var fieldAvailableList: List<String> = listOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textInputFieldLayout: TextInputLayout = view.findViewById(R.id.text_input_field_layout)
        val textInputField: AutoCompleteTextView = view.findViewById(R.id.text_input_field)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_list_field, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = fieldList[position]
        holder.textInputFieldLayout.hint = field
        holder.textInputField.setAdapter(ArrayAdapter(context, R.layout.item_dropdown_list, fieldAvailableList))
    }

    override fun getItemCount() = fieldList.size

}