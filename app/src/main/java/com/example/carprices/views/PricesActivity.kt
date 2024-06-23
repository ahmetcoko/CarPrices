package com.example.carprices.views

import android.app.AlertDialog
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
import com.example.carprices.model.ExchangeRateResponse
import com.example.carprices.network.CurrencyApiRetrofitClient
import com.example.carprices.network.CurrencyApiService
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
            finish()
        }

        binding.currencyBtn.setOnClickListener {
            fetchSupportedCurrencies()
        }


    }
    fun parseBrlCurrency(value: String): Double {
        return value.replace("R$", "").replace(".", "").replace(",", ".").trim().toDouble()
    }


    private fun fetchModelDetails(brandId: String, modelCodigo: String, selectedMotorType: String) {
        showLoading()
        val carService = RetrofitClient.instance.create(CarApiService::class.java)

        carService.getModelDetails(brandId, modelCodigo, selectedMotorType).enqueue(object :
            Callback<CarDetails> {
            override fun onResponse(call: Call<CarDetails>, response: Response<CarDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { details ->
                        showContent()
                        Log.d("PricesActivity", "Car details: $details")

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
                }
            }

            override fun onFailure(call: Call<CarDetails>, t: Throwable) {
                showError()
                t.printStackTrace()
            }
        })
    }

    private fun shareViewContent() {
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
        binding.loadingTextView.visibility = View.VISIBLE
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

    private fun fetchSupportedCurrencies() {
        val service = CurrencyApiRetrofitClient.instance.create(CurrencyApiService::class.java)
        service.getSupportedCurrencies().enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val rateOfBRL = it.conversion_rates["BRL"] ?: 1.0
                        showCurrencySelectionDialog(it.conversion_rates, rateOfBRL)
                    }
                } else {
                    Toast.makeText(this@PricesActivity, "Failed to fetch currencies", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                Toast.makeText(this@PricesActivity, "Error fetching currencies: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun onCurrencySelected(targetCurrency: String, rateOfBRL: Double, rateOfTarget: Double) {
        val currentValueText = binding.PriceValue.text.toString()
        val currentValue = parseBrlCurrency(currentValueText)
        val priceInUSD = currentValue / rateOfBRL
        val convertedValue = priceInUSD * rateOfTarget

        showConvertedValueDialog(convertedValue, targetCurrency)
    }

    private fun showConvertedValueDialog(convertedValue: Double, currency: String) {
        AlertDialog.Builder(this)
            .setTitle("Conversion Successful")
            .setMessage("The converted price in $currency is: ${String.format("%.2f", convertedValue)}")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showCurrencySelectionDialog(rates: Map<String, Double>, rateOfBRL: Double) {
        val currencyNames = rates.keys.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select a Currency")
            .setItems(currencyNames) { dialog, which ->
                val selectedCurrency = currencyNames[which]
                val rateOfTarget = rates[selectedCurrency] ?: 1.0
                onCurrencySelected(selectedCurrency, rateOfBRL, rateOfTarget)
            }
            .show()
    }





}
