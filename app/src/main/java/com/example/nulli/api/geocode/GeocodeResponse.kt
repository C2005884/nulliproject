package com.example.nulli.api.geocode


import androidx.annotation.Keep

@Keep
data class GeocodeResponse(
    val addresses: List<Addresse>,
    val errorMessage: String,
    val meta: Meta,
    val status: String
) {
    @Keep
    data class Addresse(
        val addressElements: List<AddressElement>,
        val distance: Double,
        val englishAddress: String,
        val jibunAddress: String,
        val roadAddress: String,
        val x: String,
        val y: String
    ) {
        @Keep
        data class AddressElement(
            val code: String,
            val longName: String,
            val shortName: String,
            val types: List<String>
        )
    }

    @Keep
    data class Meta(
        val count: Int,
        val page: Int,
        val totalCount: Int
    )
}