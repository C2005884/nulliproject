package com.example.nulli.model

import androidx.annotation.Keep

@Keep
class Reply (
    var id:String? = "",
    var contentId:String? = "",
    var boardId:String? = "",
    var uid:String? = "",
    var profileImageUri:String? = "",
    var nickname:String? = "",
    var content:String? = "",
    var date:Long? = 0L,
    var dateText:String? = "",

    // local view 제어용 변수
    var mine:Boolean = false
){
}

