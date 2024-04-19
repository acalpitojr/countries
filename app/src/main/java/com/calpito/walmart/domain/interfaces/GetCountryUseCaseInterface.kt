package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.domain.model.CountryData
import retrofit2.Response

interface GetCountryUseCaseInterface {
    suspend operator fun invoke(): Result<Response<List<CountryData>>>
}