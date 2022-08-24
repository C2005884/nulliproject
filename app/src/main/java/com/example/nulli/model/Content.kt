package com.example.nulli.model

import androidx.annotation.Keep

@Keep

data class Content(
    var id:String = "",
    var boardId: String = "",
    var profileImageUri:String? = "",
    var title:String? = "",
    var content:String? = "",
    var uid:String? = "",
    var nickname:String = "",
    var date:Long = 0L,
    var dateText:String="",
    var imageUri:String = "",
    var likeMap:HashMap<String, String> = hashMapOf(),
    var replyMap:HashMap<String, String> = hashMapOf(),
    var scrap:Int = 0,
    var mboard:HashMap<String, Long> = hashMapOf()
) {
}