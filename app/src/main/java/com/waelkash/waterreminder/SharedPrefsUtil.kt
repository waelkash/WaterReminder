package com.waelkash.waterreminder

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsUtil(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("water_prefs", Context.MODE_PRIVATE)

    var cupCount: Int
        get() = prefs.getInt("cup_count", 0)
        set(value) = prefs.edit().putInt("cup_count", value).apply()

    var running: Boolean
        get() = prefs.getBoolean("running", false)
        set(value) = prefs.edit().putBoolean("running", value).apply()

    var paused: Boolean
        get() = prefs.getBoolean("paused", false)
        set(value) = prefs.edit().putBoolean("paused", value).apply()

    fun reset() {
        prefs.edit().clear().apply()
    }
}
