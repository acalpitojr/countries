package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.data.model.CountryResponse
import retrofit2.Response

interface ApiInterface {
    //any api implementation should have this function for getting countries
    suspend fun getListOfCountries(): Result<Response<List<CountryResponse>>>

}