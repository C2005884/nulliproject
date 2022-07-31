package com.example.nulli.api

import com.example.nulli.api.geocode.GeocodeResponse
import com.example.nulli.api.search.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeocodeApi {
    @GET("geocode")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID:mkoqhbk1a4",
        "X-NCP-APIGW-API-KEY:pPbcUH6ANUOs43XP0FhqDZt5IsswgdjFFGZP8NTD"
    )
    fun getLatLng(@Query("query") text:String
    ): Call<GeocodeResponse>
}