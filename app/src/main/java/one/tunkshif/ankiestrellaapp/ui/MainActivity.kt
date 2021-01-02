package one.tunkshif.ankiestrellaapp.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import one.tunkshif.ankiestrellaapp.R
import one.tunkshif.ankiestrellaapp.model.Profile
import one.tunkshif.ankiestrellaapp.ui.adapter.ProfileAdapter
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val AD_PERM_REQUEST = 0
    }

    private val bottomFab: FloatingActionButton by lazy { findViewById(R.id.bottom_fab) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_profiles) }

    lateinit var profileList: List<Profile>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomFab.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        bottomFab.setOnLongClickListener {
            val intent = Intent(this, PopupActivity::class.java)
            startActivity(intent)
            true
        }

        loadProfiles()

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = ProfileAdapter(this@MainActivity, profileList)
        }

    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(this, "com.ichi2.anki.permission.READ_WRITE_DATABASE")
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GRANTED!", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf("com.ichi2.anki.permission.READ_WRITE_DATABASE"), 1)
        }
    }

    private fun loadProfiles() {
        val gson = Gson()
        val prefs = getSharedPreferences("profiles", Context.MODE_PRIVATE)
        val profiles = prefs.getStringSet("profiles", mutableSetOf())!!
        profileList = if (profiles.isEmpty()) emptyList() else
            prefs.getStringSet("profiles", mutableSetOf())!!.toMutableList().map { gson.fromJson(it, Profile::class.java) }
    }

}