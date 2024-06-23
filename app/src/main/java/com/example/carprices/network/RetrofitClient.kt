package com.example.carprices.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros/"

    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

object CurrencyApiRetrofitClient {
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/927187f55339adde036c1838/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}




