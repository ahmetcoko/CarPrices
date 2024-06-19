package com.example.carprices

data class CarModelsResponse(
    val modelos: List<CarModel>,
    val anos: List<CarYear>  // Assuming you might need this data later
)
