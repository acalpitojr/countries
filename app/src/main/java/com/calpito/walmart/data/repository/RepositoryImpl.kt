package com.calpito.walmart.data.repository

import com.calpito.walmart.domain.model.CountryData
import com.calpito.walmart.data.sources.WalmartService
import com.calpito.walmart.domain.interfaces.ApiInterface
import com.calpito.walmart.domain.interfaces.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RepositoryImpl(private val apiService: ApiInterface) : RepositoryInterface {
    //function will return success with a list of country data, or if there is some error, it will return result.failure
    override suspend fun getListOfCountries(): Result<List<CountryData>> {
        //call the api to get countries
        var countryResult = Result.success(emptyList<CountryData>()) //default

        val result = apiService.getListOfCountries()
        result.fold(onSuccess = {
            //process the api response
            if (it.isSuccessful) {
                //api success, try to get the data out.
                try {
                    val listOfCountries = it.body()!!
                    countryResult = Result.success(listOfCountries)
                } catch (e: Exception) {
                    countryResult = Result.failure(e)
                }

                countryResult
            } else {
                //failed api
                val errorMsg = it.errorBody()?.string()
                countryResult = Result.failure(Exception(errorMsg))
            }
        }, onFailure = {
            countryResult = Result.failure(it)
        })


        return countryResult

    }
}