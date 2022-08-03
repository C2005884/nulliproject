package com.example.nulli.api

import android.util.Log
import com.example.nulli.api.geocode.GeocodeResponse
import com.example.nulli.api.reverse_geocoding.ReverseGeocodingResponse
import com.example.nulli.api.search.SearchResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naver.maps.geometry.LatLng
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CallApi {

    private val gson: Gson? = GsonBuilder().setLenient().create()
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/v1/")
        .addConverterFactory(GsonConverterFactory.create(gson!!))
        .client(client)
        .build()

    private val naverMapRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/v1/")
        .addConverterFactory(GsonConverterFactory.create(gson!!))
        .client(client)
        .build()

    private val geocodeRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://naveropenapi.apigw.ntruss.com/")
        .addConverterFactory(GsonConverterFactory.create(gson!!))
        .client(client)
        .build()

    fun getLatLng(address:String, callback:(GeocodeResponse.Addresse?) -> Unit) {
        geocodeRetrofit.create(GeocodeApi::class.java)
            .getLatLng(address)
            .enqueue(object  : Callback<GeocodeResponse> {
                override fun onResponse(
                    call: Call<GeocodeResponse>,
                    response: Response<GeocodeResponse>
                ) {
                    callback(response.body()?.addresses?.get(0))
                }

                override fun onFailure(call: Call<GeocodeResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getLocation(latlng: LatLng, callback:(String?) -> Unit) {
        geocodeRetrofit.create(GeocodeApi::class.java)
            .getLocation(latlng.longitude.toString()+","+latlng.latitude.toString())
            .enqueue(object  : Callback<ReverseGeocodingResponse> {
                override fun onResponse(
                    call: Call<ReverseGeocodingResponse>,
                    response: Response<ReverseGeocodingResponse>
                ) {
                    callback(response.body()?.getLocation())
                }

                override fun onFailure(call: Call<ReverseGeocodingResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun search(searchString:String, callback:(List<SearchResponse.Item>?) -> Unit){
        retrofit.create(NaverMapApi::class.java)
            .searchPlace(searchString)
            .enqueue(object :
                Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    //Log.e("res","${response.body()}")
                    callback(response.body()?.items)
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

            })

    }
}