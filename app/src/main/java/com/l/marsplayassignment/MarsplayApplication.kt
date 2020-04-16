package com.l.marsplayassignment


import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

fun string(@StringRes id: Int): String = MarsplayApplication.instance.resources.getString(id)

fun color(@ColorRes id: Int): Int = ResourcesCompat.getColor(MarsplayApplication.instance.resources, id, null)

@SuppressLint("Registered")
open class MarsplayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        lateinit var instance: Application
            private set

        lateinit var pref: SharedPreferences

    }
}

