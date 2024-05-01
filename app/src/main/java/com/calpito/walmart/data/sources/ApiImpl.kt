package com.calpito.walmart.data.sources

import com.calpito.walmart.domain.interfaces.ApiInterface
import com.calpito.walmart.domain.model.CountryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ApiImpl:ApiInterface {
    override suspend fun getListOfCountries(): Result<Response<List<CountryData>>> {
        //call the api.  catch any exceptions
        val result = withContext(Dispatchers.IO){
            try {
                val result = WalmartService.getService().getCountries()
                Result.success(result)
            } catch (e:Exception){
                Result.failure(e)
            }
        }

        return result

    }
}