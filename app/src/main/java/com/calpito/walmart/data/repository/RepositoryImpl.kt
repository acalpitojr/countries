package com.calpito.walmart.data.repository

import com.calpito.walmart.domain.interfaces.ApiInterface
import com.calpito.walmart.domain.interfaces.RepositoryInterface
import com.calpito.walmart.domain.model.RecyclerData


class RepositoryImpl(private val apiService: ApiInterface) : RepositoryInterface {
    //function will return success with a list of country data, or if there is some error, it will return result.failure
    override suspend fun getListOfCountries(): Result<List<RecyclerData.CountryData>> {
        //call the api to get countries
        var countryResult = Result.success(emptyList<RecyclerData.CountryData>()) //default

        val result = apiService.getListOfCountries()
        result.fold(onSuccess = {
            //process the api response
            if (it.isSuccessful) {
                //api success, try to get the data out.
                try {
                    val listOfCountriesResponse = it.body()!!
                    val listOfCountries = listOfCountriesResponse.map {
                        RecyclerData.CountryData(
                            capital = it.capital,
                            code = it.code,
                            currency = it.currency,
                            flag = it.flag,
                            language = it.language,
                            name = it.name,
                            region = it.region,
                        )
                    }
                    //create listOf<CountryData> from listOfCountryResponse
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