package com.example.nulli.model

import androidx.annotation.Keep

@Keep
data class Review(
    var id:String? = "",
    var buildingId:String? = "",
    var content:String? = "",
    var date:Long? = 0L,
    var dateText:String? = "",

    var  imageUrl:String? = "",
    var nickname:String? = "",
    var profileImageUri:String? = "",
    var uid:String? = ""
)

