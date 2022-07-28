package com.example.nulli.api.search


import androidx.annotation.Keep

@Keep
data class SearchResponse(
    val display: Int,
    val items: List<Item>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
) {
    @Keep
    data class Item(
        val address: String,
        val category: String,
        val description: String,
        val link: String,
        val mapx: String,
        val mapy: String,
        val roadAddress: String,
        val telephone: String,
        val title: String
    )
}