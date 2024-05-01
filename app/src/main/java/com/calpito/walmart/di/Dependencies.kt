package com.calpito.walmart.di

import com.calpito.walmart.data.repository.RepositoryImpl
import com.calpito.walmart.data.sources.ApiImpl
import com.calpito.walmart.domain.usecase.GetCountriesUseCase

object Dependencies {

    val getCountriesUseCase by lazy { GetCountriesUseCase(getRepository)}

    val getApiImpl by lazy { ApiImpl() }

    val getRepository by lazy { RepositoryImpl(getApiImpl)}
}