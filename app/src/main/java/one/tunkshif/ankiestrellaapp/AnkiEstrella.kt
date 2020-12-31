package one.tunkshif.ankiestrellaapp

import android.app.Application
import android.content.Context
import okhttp3.OkHttpClient

class AnkiEstrella : Application() {
    companion object {
        lateinit var application: Application
        lateinit var context: Context
        val okHttpClient by lazy { OkHttpClient() }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        context = applicationContext
    }

}