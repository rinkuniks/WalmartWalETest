package com.example.walmartwaletest.Utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceHelper {

    fun preference(context: Context, fileName: String): SharedPreferences =
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun SharedPreferences.myEdit(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }


    var SharedPreferences.title: String?
        get() = getString(TITLE, null)
        set(value) {
            myEdit {
                it.putString(TITLE, value)
            }
        }

    var SharedPreferences.description: String?
        get() = getString(DESCRIPTION, null)
        set(value) {
            myEdit {
                it.putString(DESCRIPTION, value)
            }
        }

    var SharedPreferences.lastUpdatedDate: String?
        get() = getString(LASTUPDATE,null)
        set(value) {
            myEdit {
                it.putString(LASTUPDATE, value)
            }
        }
}