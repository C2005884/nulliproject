package com.example.nulli.util

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val PREFERENCES_FILE_NAME = "NULLI_SHARED_PREFERENCES_MANAGER"
    private var pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)


    // key 목록
    private val SEARCH_HISTORY_SET = "SEARCH_HISTORY_SET"

    // value 목록
    var searchHistorySet: MutableSet<String>
        get() = pref.getStringSet(SEARCH_HISTORY_SET, mutableSetOf()) ?: mutableSetOf()
        set(value) = pref.edit().putStringSet(SEARCH_HISTORY_SET, value).apply()

    fun addSearchHistorySet(value: String) {
        val set: MutableSet<String> = mutableSetOf()
        for (i in searchHistorySet) {
            set.add(i)
        }
        set.add(value)
        searchHistorySet = set
    }

    fun removeSearchHistorySet(value: String) {
        val set: MutableSet<String> = mutableSetOf()
        for (i in searchHistorySet) {
            set.add(i)
        }
        set.remove(value)
        searchHistorySet = set
    }
}