package com.example.carprices.model

data class ExchangeRateResponse(
    val result: String,
    val conversion_rates: Map<String, Double>
)

