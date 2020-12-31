package one.tunkshif.ankiestrellaapp.utils

import android.widget.Toast
import one.tunkshif.ankiestrellaapp.AnkiEstrella

fun makeToast(text: String) = Toast.makeText(AnkiEstrella.context, text, Toast.LENGTH_SHORT).show()

fun getStringFromResources(resourceId: Int) = AnkiEstrella.context.getString(resourceId)
