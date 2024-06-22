package com.example.carprices.network

import com.example.carprices.model.ExchangeRateResponse
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("latest/USD")
    fun getSupportedCurrencies(): Call<ExchangeRateResponse>
}


