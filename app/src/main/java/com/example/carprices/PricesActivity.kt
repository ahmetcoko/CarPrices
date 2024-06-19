package com.example.carprices

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carprices.databinding.ActivityPricesBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PricesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPricesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPricesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val brandId = intent.getStringExtra("brandId") ?: ""
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""
        val selectedMotorType = intent.getStringExtra("selectedMotorType") ?: ""

        fetchModelDetails(brandId, modelCodigo, selectedMotorType)
    }

    private fun fetchModelDetails(brandId: String, modelCodigo: String, selectedMotorType: String) {
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        val call = carService.getModelDetails(brandId, modelCodigo, selectedMotorType)

        // Log the URL
        Log.d("PricesActivity", "API call: ${call.request().url()}")
        carService.getModelDetails(brandId, modelCodigo, selectedMotorType).enqueue(object :
            Callback<CarDetails> {
            override fun onResponse(call: Call<CarDetails>, response: Response<CarDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { details ->

                        Log.d("PricesActivity", "Car details: $details")
                        // Setting text from API response to TextViews
                        binding.vehicleTypeValue.text = details.tipoVeiculo.toString()
                        binding.brandValue.text = details.marca
                        binding.modelValue.text = details.modelo
                        binding.modelYearValue.text = details.anoModelo.toString()
                        binding.fuelTypeValue.text = details.combustivel
                        binding.fipeCodeValue.text = details.codigoFipe
                        binding.referenceMonthValue.text = details.mesReferencia
                        binding.fuelInitialsValue.text = details.siglaCombustivel
                        binding.PriceValue.text = details.valor
                    }
                } else {
                    val gson = Gson()
                    val errorBody = response.errorBody()?.string()
                    val errorResponse: ErrorResponse? = gson.fromJson(errorBody, ErrorResponse::class.java)
                    Log.e("PricesActivity", "Failed to fetch car details: ${errorResponse?.error}")
                    // Show error to user or handle it appropriately
                }
            }

            override fun onFailure(call: Call<CarDetails>, t: Throwable) {
                Log.e("PricesActivity", "Error fetching car details: ${t.message}")
                t.printStackTrace()
            }
        })
    }

}
