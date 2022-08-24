package com.example.nulli.model

import androidx.annotation.Keep

@Keep
data class UserData(
    var uid:String? = "",
    var email:String? = "",
    var nickname:String? = "",
    var profileImageUri:String? = "",
    var scrapMap:HashMap<String, String>? = hashMapOf(),
    var myContentMap:HashMap<String, String>? = hashMapOf()
) {
}