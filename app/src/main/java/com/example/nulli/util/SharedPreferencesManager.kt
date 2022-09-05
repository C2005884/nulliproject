package com.example.nulli.util

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val PREFERENCES_FILE_NAME = "NULLI_SHARED_PREFERENCES_MANAGER"
    private var pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)


    // key 목록
    private val SEARCH_HISTORY_SET = "SEARCH_HISTORY_SET"
    private val SEARCH_HISTORY_LIST = "SEARCH_HISTORY_LIST"

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
    //println(searchHistoryList) = println(pref.getString((SEARCH_HISTORY_LIST,"[]")?:"[]")
    var searchHistoryList: String
    get() = pref.getString(SEARCH_HISTORY_LIST,"[]")?:"[]"
    set(value) = pref.edit().putString(SEARCH_HISTORY_LIST,value).apply()

    fun loadSearchHistoryList(): ArrayList<String>{
        return ArrayList(
            searchHistoryList
                .replace("[","")
                .replace("]","")
                .split(", "))
    }

    fun addSearchHistoryList(value:String){
        val list = searchHistoryList // [] , [asdf], [fef],[fesf]
        // [fesf
        val addedString = "${list.substring(0, list.lastIndex)}, $value]"
        //[fesf, value]
        searchHistoryList = addedString
    }

    fun removeSearchHistoryList(value: String){
        val list = searchHistoryList.replace(value, "").replace(", ,", ",")
        searchHistoryList = list
    }
}