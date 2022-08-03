package com.example.nulli.model

import androidx.annotation.Keep

@Keep

data class Obstacle(
    var id:String = "",
    var latitude:String? = "",
    var longitude:String? = "",
    var name: String? = "",
    var address: String? = "",
    var type: String? = "",
    var uid:String? = "",
    var nickname:String = "",
    var date:Long = 0L,
    var imageUri:String = "",
) {
    companion object {
        const val STAIR = 0
        const val BLOCK = 1
        const val SLOPE = 2
        const val OTHER = 3

    }
}