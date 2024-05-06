package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.domain.model.RecyclerData
import retrofit2.Response

interface GetCountryUseCaseInterface {
    suspend operator fun invoke(): Result<List<RecyclerData.CountryData>>
}