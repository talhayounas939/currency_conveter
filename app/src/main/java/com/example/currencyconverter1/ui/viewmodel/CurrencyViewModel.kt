package com.example.currencyconverter1.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.data.repository.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val ratesRepository: RatesRepository
) : ViewModel() {

    private val _fromCurrency = MutableStateFlow<CountryWithRate?>(null)
    val fromCurrency: StateFlow<CountryWithRate?> = _fromCurrency.asStateFlow()

    private val _toCurrency = MutableStateFlow<CountryWithRate?>(null)
    val toCurrency: StateFlow<CountryWithRate?> = _toCurrency.asStateFlow()

    private val _amount = MutableStateFlow("1")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _rate = MutableStateFlow(0.0)
    val rate: StateFlow<Double> = _rate.asStateFlow()

    private val _isLoadingRates = MutableStateFlow(false)
    val isLoadingRates: StateFlow<Boolean> = _isLoadingRates.asStateFlow()

    fun setCurrencies(from: CountryWithRate, to: CountryWithRate) {
        _fromCurrency.value = from
        _toCurrency.value = to
        calculateRate(from, to)
    }

    fun updateAmount(key: String) {
        _amount.value = when (key) {
            "Clear" -> ""
            "." -> if (_amount.value.contains(".")) _amount.value else _amount.value + key
            else -> {
                if (_amount.value == "0") key else _amount.value + key
            }
        }
        // Recalculate conversion when amount changes
        fromCurrency.value?.let { from ->
            toCurrency.value?.let { to ->
                calculateRate(from, to)
            }
        }
    }

    private fun calculateRate(from: CountryWithRate, to: CountryWithRate) {
        _rate.value = ratesRepository.getRate(from, to)
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