package com.l.marsplayassignment.views.baseView


import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

//global access to gson object
val gson = Gson()

/*
Some utility functions which reduces lot of boilerplate from our codebase to access resources
 */
fun string(@StringRes id: Int): String = MarsplayApplication.instance.resources.getString(id)

fun color(@ColorRes id: Int): Int = ResourcesCompat.getColor(MarsplayApplication.instance.resources, id, null)
fun font(@FontRes id: Int): Typeface = ResourcesCompat.getFont(MarsplayApplication.instance, id)
        ?: Typeface.DEFAULT

fun dimen(@DimenRes id: Int): Float = MarsplayApplication.instance.resources.getDimension(id)
fun integer(@IntegerRes id: Int): Int = MarsplayApplication.instance.resources.getInteger(id)
fun drawable(@DrawableRes id: Int): Drawable? = ResourcesCompat.getDrawable(MarsplayApplication.instance.resources, id, null)


@SuppressLint("Registered")
open class MarsplayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Realm.init(this)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

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

