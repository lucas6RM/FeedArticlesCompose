package com.mercierlucas.feedarticlescompose.data.local

import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPrefs @Inject constructor(val prefs: SharedPreferences) {

    companion object{
        const val PREF_FILENAME = "com.mercierlucas.feedarticlescompose.prefs"
        const val TOKEN = "token"
        const val USER_ID = "userId"
    }

    var userId: Long
        get() = prefs.getLong(USER_ID, 0L)
        set(value) = prefs.edit().putLong(USER_ID, value).apply()

    var token: String?
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()

}