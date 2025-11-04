package com.example.currencyconverter1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.data.repository.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstSelectionViewModel @Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    private val _countries = MutableStateFlow<List<CountryWithRate>>(emptyList())
    val countries: StateFlow<List<CountryWithRate>> = _countries.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val countriesList = ratesRepository.getCountriesWithLiveRates("USD")
                _countries.value = countriesList
            } catch (e: Exception) {
                e.printStackTrace()
                _countries.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}