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
class CurrencyViewModel @Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    private val _countries = MutableStateFlow<List<CountryWithRate>>(emptyList())
    val countries: StateFlow<List<CountryWithRate>> = _countries.asStateFlow()

    private val _fromCurrency = MutableStateFlow<CountryWithRate?>(null)
    val fromCurrency: StateFlow<CountryWithRate?> = _fromCurrency.asStateFlow()

    private val _toCurrency = MutableStateFlow<CountryWithRate?>(null)
    val toCurrency: StateFlow<CountryWithRate?> = _toCurrency.asStateFlow()

    private val _amount = MutableStateFlow("1")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _rate = MutableStateFlow(0.0)
    val rate: StateFlow<Double> = _rate.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoadingRates = MutableStateFlow(false)
    val isLoadingRates: StateFlow<Boolean> = _isLoadingRates.asStateFlow()

    init {
        loadCountries()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _isLoadingRates.value = true
            _countries.value = ratesRepository.getCountriesWithLiveRates()
            _isLoadingRates.value = false
        }
    }

    fun setFromCurrency(from: CountryWithRate?) {
        _fromCurrency.value = from
        recalculateRate()
    }

    fun setToCurrency(to: CountryWithRate?) {
        _toCurrency.value = to
        recalculateRate()
    }

    fun setCurrencies(from: CountryWithRate, to: CountryWithRate) {
        _fromCurrency.value = from
        _toCurrency.value = to
        recalculateRate()
    }

    fun swapCurrencies() {
        val from = _fromCurrency.value
        _fromCurrency.value = _toCurrency.value
        _toCurrency.value = from
        recalculateRate()
    }

    fun updateAmount(key: String) {
        val currentAmount = _amount.value
        when (key) {
            "Backspace" -> _amount.value = if (currentAmount.isNotEmpty()) currentAmount.dropLast(1) else ""
            "." -> {
                if (currentAmount.contains(".")) {
                    _error.value = "Invalid input: Multiple decimal points"
                } else {
                    _amount.value += key
                }
            }
            "00" -> {
                if (currentAmount.isNotEmpty() && currentAmount != "0") {
                    _amount.value += "00"
                }
            }
            else -> {
                _amount.value = if (currentAmount == "0") key else currentAmount + key
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun recalculateRate() {
        val from = _fromCurrency.value
        val to = _toCurrency.value
        if (from != null && to != null) {
            _rate.value = ratesRepository.getRate(from, to)
        }
    }

    fun getConvertedAmount(): String {
        val amountValue = _amount.value.toDoubleOrNull() ?: 0.0
        val from = _fromCurrency.value
        val to = _toCurrency.value

        return if (from != null && to != null && amountValue > 0) {
            val converted = ratesRepository.convertAmount(amountValue, from, to)
            String.format("%.2f", converted)
        } else {
            "0.00"
        }
    }
}