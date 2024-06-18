package com.example.carprices

import retrofit2.Call
import retrofit2.http.GET

interface CarApiService {
    @GET("marcas")  // Removed the leading slash
    fun getCarBrands(): Call<List<CarBrand>>
}

