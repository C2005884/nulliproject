package com.example.nulli.model

import androidx.annotation.Keep

@Keep

data class Board(
    var id:String = "",
    var profileImageUri:String? = "",
    var title:String? = "",
    var content:String? = "",
    var uid:String? = "",
    var nickname:String = "",
    var date:Long = 0L,
    var dateText:String="",
    var imageUri:String = "",
    var heart:Int = 0,
    var commentcount:Int = 0,
    var scrap:Int = 0,
    var mboard:HashMap<String, Long> = hashMapOf()
)