package one.tunkshif.ankiestrellaapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Profile

class ProfileAdapter(private val context: Context, private val profileList: List<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileCardView: MaterialCardView = view.findViewById(R.id.cardview_profile)
        val profileNameView: TextView = view.findViewById(R.id.profile_name)
        val profileImageView: ImageView = view.findViewById(R.id.profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder = ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_profile, parent, false)
        )
        viewHolder.profileCardView.setOnClickListener {
            Toast.makeText(context, viewHolder.profileNameView.text, Toast.LENGTH_LONG).show()
        }
        viewHolder.profileCardView.setOnLongClickListener {
            showPopupMenu(it)
            true
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profileList[position]
        holder.profileNameView.text = profile.name
        holder.profileImageView.setImageResource(profile.imageId)
    }

    override fun getItemCount() = profileList.size

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.apply {
            menuInflater.inflate(R.menu.profile_pop_up_menu, popupMenu.menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.profile_pop_up_menu_item_edit -> {
                        Toast.makeText(context, "Edit", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.profile_pop_up_menu_item_delete -> {
                        Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
}