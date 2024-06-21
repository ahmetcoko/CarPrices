package com.example.carprices.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.carprices.network.CarApiService
import com.example.carprices.network.RetrofitClient
import com.example.carprices.databinding.ActivityPricesBinding
import com.example.carprices.model.CarDetails
import com.example.carprices.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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

        binding.ShareBtn.setOnClickListener {
            shareViewContent()
        }

        binding.retryButton.setOnClickListener {
            fetchModelDetails(brandId, modelCodigo, selectedMotorType)
        }

        binding.backButton.setOnClickListener {
            finish()  // Close this activity and return to the previous one
        }

    }

    private fun fetchModelDetails(brandId: String, modelCodigo: String, selectedMotorType: String) {
        showLoading()
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        val call = carService.getModelDetails(brandId, modelCodigo, selectedMotorType)

        // Log the URL
        Log.d("PricesActivity", "API call: ${call.request().url()}")
        carService.getModelDetails(brandId, modelCodigo, selectedMotorType).enqueue(object :
            Callback<CarDetails> {
            override fun onResponse(call: Call<CarDetails>, response: Response<CarDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { details ->
                        showContent()
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
                    showError()
                    val gson = Gson()
                    val errorBody = response.errorBody()?.string()
                    val errorResponse: ErrorResponse? = gson.fromJson(errorBody, ErrorResponse::class.java)
                    Log.e("PricesActivity", "Failed to fetch car details: ${errorResponse?.error}")
                    // Show error to user or handle it appropriately
                }
            }

            override fun onFailure(call: Call<CarDetails>, t: Throwable) {
                showError()
                Log.e("PricesActivity", "Error fetching car details: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun shareViewContent() {
        val bitmap = Bitmap.createBitmap(binding.root.width, binding.root.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        binding.root.draw(canvas)

        try {
            val file = File(externalCacheDir, "prices_screenshot.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val fileUri = FileProvider.getUriForFile(this, "${packageName}.provider", file)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, fileUri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to share the image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE // Show the loading text
        binding.errorTextView.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
    }


    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.retryButton.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.GONE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.loadingTextView.visibility = View.GONE
    }


}
