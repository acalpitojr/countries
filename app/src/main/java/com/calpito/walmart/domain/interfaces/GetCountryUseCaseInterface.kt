package com.calpito.walmart.domain.interfaces

import com.calpito.walmart.domain.model.CountryData

interface GetCountryUseCaseInterface {
    suspend operator fun invoke(): Result<List<CountryData>>
}