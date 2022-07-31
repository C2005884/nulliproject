package com.example.nulli.api

import com.example.nulli.api.search.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverMapApi {
    @GET("search/local.json")
    @Headers(
        "X-Naver-Client-Id:lMy0Qj3m5UAUKXdMR0x3",
        "X-Naver-Client-Secret:fztE84ggf9"
    )
    fun searchPlace(@Query("query") text:String,
                    @Query("display") display:Int = 10,
                    @Query("start") start:Int = 1,
                    @Query("sort") sort:String = "random"
    ): Call<SearchResponse>


}