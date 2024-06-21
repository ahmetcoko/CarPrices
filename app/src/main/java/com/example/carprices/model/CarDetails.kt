package com.example.carprices.model

import com.google.gson.annotations.SerializedName

data class CarDetails(
    @SerializedName("TipoVeiculo") val tipoVeiculo: Int,
    @SerializedName("Valor") val valor: String?,
    @SerializedName("Marca") val marca: String?,
    @SerializedName("Modelo") val modelo: String?,
    @SerializedName("AnoModelo") val anoModelo: Int,
    @SerializedName("Combustivel") val combustivel: String?,
    @SerializedName("CodigoFipe") val codigoFipe: String?,
    @SerializedName("MesReferencia") val mesReferencia: String?,
    @SerializedName("SiglaCombustivel") val siglaCombustivel: String?
)


