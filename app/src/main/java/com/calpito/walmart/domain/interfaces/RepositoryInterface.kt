package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.domain.model.CountryData
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getListOfCountries(): Result<List<CountryData>>
}