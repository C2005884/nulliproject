package com.example.nulli.model

import androidx.annotation.Keep

@Keep

data class Building(
    var id:String = "",
    var title:String? = "",
    var latitude:String? = "",
    var longitude:String? = "",
    var address: String? = "",
    var type: String? = "",
    var uid:String? = "",
    var nickname:String = "",
    var date:Long = 0L,
    var dateText:String="",
    var imageUri:String = "",
    var review:HashMap<String, Long> = hashMapOf(),
    var activation:String? = "",
) {
    companion object {
        const val HOSPITAL = 0
        const val DRUG = 1
        const val REHABILITATION = 2
        const val VRIOUS = 3

    }
}