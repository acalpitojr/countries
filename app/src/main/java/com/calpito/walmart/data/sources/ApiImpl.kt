package com.calpito.walmart.data.sources

import com.calpito.walmart.data.model.CountryResponse
import com.calpito.walmart.domain.interfaces.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ApiImpl:ApiInterface {
    override suspend fun getListOfCountries(): Result<Response<List<CountryResponse>>> {
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