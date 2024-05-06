package com.calpito.walmart.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calpito.walmart.domain.interfaces.GetCountryUseCaseInterface
import com.calpito.walmart.domain.model.RecyclerData
import com.calpito.walmart.domain.model.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val getCountriesUseCase: GetCountryUseCaseInterface) : ViewModel() {
    //List for recyclerView
    private val _recyclerData = MutableStateFlow<UIState<List<RecyclerData>>>(UIState.Loading)
    val recyclerData: StateFlow<UIState<List<RecyclerData>>> = _recyclerData


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
            _recyclerData.value = UIState.Loading
            val result = getCountriesUseCase()
            result.fold(
                onSuccess = {
                    //here we can format the data before setting it for the UI.

                    //we need to create a list for the recyclerView.
                    //our recycler view will be  list of countries, but before each first character, we will have a header, which is the first letter of the country.
                    //example, A, america, afdjskl, afjdkf, B, bfesj, bkvjdk, ETC
                    //first sort the list
                    it.sortedBy { it.name }
                    //itterate through all countries.  create a header if we are at a new letter, add country to the list
                    val recyclerList = mutableListOf<RecyclerData>()
                    var currentHeaderChar: Char? = null
                    for (country in it) {
                        val firstChar = country.name?.get(0)
                        if (firstChar != currentHeaderChar) {
                            currentHeaderChar = firstChar
                            //add a header to the list
                            val header = RecyclerData.CountryHeader("$currentHeaderChar")
                            recyclerList.add(header)
                        }

                        recyclerList.add(
                            RecyclerData.CountryData(
                                name = country.name,
                                code = country.code,
                                currency = country.currency,
                                flag = country.flag,
                                language = country.language,
                                region = country.region,
                                capital = country.capital
                            )
                        )
                    }

                    _recyclerData.value = UIState.Success(recyclerList)
                },
                onFailure = {
                    _recyclerData.value = UIState.Error(it.toString())
                })
        }
    }
}