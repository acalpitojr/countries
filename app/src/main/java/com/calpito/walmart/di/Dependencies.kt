package com.calpito.walmart.di

import com.calpito.walmart.data.repository.RepositoryImpl
import com.calpito.walmart.domain.usecase.GetCountriesUseCase

object Dependencies {
    val getCountriesUseCase by lazy { GetCountriesUseCase(RepositoryImpl) }
}