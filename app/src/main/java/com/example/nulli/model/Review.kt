package com.example.nulli.model

import androidx.annotation.Keep

@Keep
data class Review(
    var id:String? = "",
    var latitude:String? = "",
    var longitude:String? = "",
    var address: String? = "",
    var type: String? = "",
    var buildingId:String? = "",
    var content:String? = "",
    var date:Long? = 0L,
    var dateText:String? = "",
    var imageUrl:String? = "",
    var nickname:String? = "",
    var profileImageUri:String? = "",
    var uid:String? = "",
    var imageUri:String = "",
){
    companion object {
        const val HOSPITAL = 0
        const val DRUG = 1
        const val REHABILITATION = 2
        const val VRIOUS = 3

    }
}

