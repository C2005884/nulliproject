package com.example.nulli.model

import androidx.annotation.Keep

@Keep
data class Review(
    var id:String? = "",
    var buildingId:String? = "",
    var content:String? = "",
    var date:Long? = 0L,
    var dateText:String? = "",
    var imageUri:String? = "",
    var nickname:String? = "",
    var profileImageUri:String? = "",
    var uid:String? = "",
){
    companion object {
        const val HOSPITAL = 0
        const val DRUG = 1
        const val REHABILITATION = 2
        const val VRIOUS = 3

    }
}

