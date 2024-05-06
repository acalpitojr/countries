package com.calpito.walmart.data.sources

import com.calpito.walmart.data.model.CountryResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface WalmartApiInterface {
    /*https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json*/
    @GET("32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
    suspend fun getCountries(): Response<List<CountryResponse>>
}

object WalmartService {
    //setup the retrofit client
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpCLient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    //Retrofit client for the given base url
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/peymano-wmt/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpCLient)
        .build()

    //create implementation for ApiService.  Used for actual calls
    private val _walmartService = retrofit.create(WalmartApiInterface::class.java)

    fun getService(): WalmartApiInterface {
        return _walmartService
    }
}