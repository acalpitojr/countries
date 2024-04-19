package com.calpito.walmart.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calpito.walmart.domain.model.CountryData
import com.calpito.walmart.domain.interfaces.GetCountryUseCaseInterface
import com.calpito.walmart.domain.model.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val getCountriesUseCase: GetCountryUseCaseInterface) : ViewModel() {
    //List for recyclerView
    private val _countries = MutableStateFlow<UIState<List<CountryData>>>(UIState.Loading)
    val countries: StateFlow<UIState<List<CountryData>>> = _countries


    init {
        getCountries()
    }

    /**
     * Fetches country data asynchronously and updates UI state based on the outcome.
     * This function sets the UI state to Loading, executes the getCountries use case,
     * and handles the results as follows:
     *
     * - OnSuccess: If the API call is successful, it sets the UI state to Success with the country list,
     *   otherwise, it sets it to Error with the appropriate HTTP error message.
     * - OnFailure: It sets the UI state to Error, detailing any exceptions that occurred during the API call.
     */
    fun getCountries() {
        viewModelScope.launch {
            _countries.value = UIState.Loading
            val result = getCountriesUseCase()
            result.fold(
                onSuccess = { response ->
                    if (response.isSuccessful) {
                        val countries = response.body() ?: emptyList()
                        _countries.value = UIState.Success(countries)
                    } else {
                        val errorMessage = "${response.code()} ${response.errorBody()}"
                        _countries.value = UIState.Error(errorMessage)
                    }
                },
                onFailure = { exception ->
                    // Handle exceptions that occurred during the network call
                    _countries.value = UIState.Error("Exception occurred: ${exception.message}")
                }
            )
        }
    }
}