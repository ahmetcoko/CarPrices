package com.example.carprices.model

import com.example.carprices.model.CarModel
import com.example.carprices.model.CarYear

data class CarModelsResponse(
    val modelos: List<CarModel>,
    val anos: List<CarYear>  // Assuming you might need this data later
)
