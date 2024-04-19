package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.domain.model.CountryData
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getCountries(): Result<Response<List<CountryData>>>
}