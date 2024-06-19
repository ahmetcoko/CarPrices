package com.example.carprices

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CarApiService {
    @GET("marcas")
    fun getCarBrands(): Call<List<CarBrand>>

    @GET("marcas/{brandCodigo}/modelos")
    fun getCarModels(@Path("brandCodigo") brandCodigo: String): Call<CarModelsResponse>

    @GET("marcas/{marcaId}/modelos/{modeloId}/anos")
    fun getMotorTypes(@Path("marcaId") marcaId: String, @Path("modeloId") modeloId: String): Call<List<MotorType>>

    @GET("marcas/{marcaId}/modelos/{modeloId}/anos/{anoCodigo}")
    fun getModelDetails(@Path("marcaId") marcaId: String, @Path("modeloId") modeloId: String, @Path("anoCodigo") anoCodigo: String): Call<CarDetails>
}



