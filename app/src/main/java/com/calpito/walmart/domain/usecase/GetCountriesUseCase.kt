package com.calpito.walmart.domain.usecase


import com.calpito.walmart.domain.interfaces.GetCountryUseCaseInterface
import com.calpito.walmart.domain.interfaces.RepositoryInterface
import com.calpito.walmart.domain.model.RecyclerData
import retrofit2.Response

class GetCountriesUseCase(private val repository: RepositoryInterface): GetCountryUseCaseInterface {
    override suspend operator fun invoke(): Result<List<RecyclerData.CountryData>> = repository.getListOfCountries()
}