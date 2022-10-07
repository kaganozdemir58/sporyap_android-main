package com.sporyap.sporyap.utils

import android.content.Context
import androidx.core.content.edit
import org.jetbrains.anko.defaultSharedPreferences

class Prefs {

    companion object Prefs {
        fun setStringListSharedPreferences(
            context: Context?,
            value: ArrayList<String>,
            key: String
        ) {
            val set: MutableSet<String> = HashSet()
            set.addAll(value)
            context!!.applicationContext.defaultSharedPreferences.edit {
                putStringSet(key, set)
            }
        }

        fun getStringListSharedPreferences(context: Context?, key: String): MutableSet<String>? {
            return context!!.applicationContext.defaultSharedPreferences.getStringSet(key, null)
        }

        fun setKeySharedPreferences(context: Context, key: String, value: String) {
            context.applicationContext.defaultSharedPreferences.edit {
                putString(key, value).apply()
            }
        }

        fun getKeySharedPreferences(context: Context, key: String): String {
            return context.applicationContext.defaultSharedPreferences.getString(key, null) ?: ""
        }

        fun setKeySharedPreferencesBoolean(context: Context, key: String, value: Boolean) {
            context.applicationContext.defaultSharedPreferences.edit {
                putBoolean(key, value).apply()
            }
        }

        fun getKeySharedPreferencesBoolean(context: Context, key: String): Boolean {
            return context.applicationContext.defaultSharedPreferences.getBoolean(key, false)
        }

        fun setKeySharedPreferencesInt(context: Context, key: String, value: Int) {
            context.applicationContext.defaultSharedPreferences.edit {
                putInt(key, value).apply()
            }
        }

        fun getKeySharedPreferencesInt(context: Context, key: String): Int {
            return context.applicationContext.defaultSharedPreferences.getInt(key, 0)
        }

        fun removeSharedPreferences(context: Context) {
            context.applicationContext.defaultSharedPreferences.edit().clear().apply()
        }
    }
}