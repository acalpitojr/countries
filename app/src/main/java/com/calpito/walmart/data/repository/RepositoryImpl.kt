package com.calpito.walmart.data.repository

import com.calpito.walmart.domain.model.CountryData
import com.calpito.walmart.data.sources.WalmartService
import com.calpito.walmart.domain.interfaces.RepositoryInterface
import retrofit2.Response

object RepositoryImpl: RepositoryInterface {
    override suspend fun getCountries(): Result<Response<List<CountryData>>> {
        return try {
            // Attempt to get the list of countries from the service.
            val response = WalmartService.getService().getCountries()
            Result.success(response)
        } catch (e: Exception) {
            // Log the exception
            println("Failed to fetch countries: ${e.message}")

            // Return a Result.failure encapsulating the exception
            Result.failure(e)
        }
    }
}