package com.example.carprices.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.adapter.MotorTypeAdapter
import com.example.carprices.network.CarApiService
import com.example.carprices.network.RetrofitClient
import com.example.carprices.databinding.ActivityMotorTypesBinding
import com.example.carprices.model.MotorType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MotorTypesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMotorTypesBinding
    private lateinit var adapter: MotorTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MotorTypesActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        binding = ActivityMotorTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView() // Set up the RecyclerView

        val modelName = intent.getStringExtra("modelName") ?: "Unknown"
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""
        val brandId = intent.getStringExtra("brandId") ?: "" // Ensure you're passing this from the previous activity

        Log.d("MotorTypesActivity", "brandId: $brandId, modelCodigo: $modelCodigo") // Add this line

        binding.ModelMotorsTitleTextView.text = modelName
        if (brandId.isNotEmpty() && modelCodigo.isNotEmpty()) {
            fetchMotorTypes(brandId, modelCodigo)
        }

        binding.retryButton.setOnClickListener {
            fetchMotorTypes(brandId, modelCodigo)
        }

        binding.backButton.setOnClickListener {
            finish()  // Close this activity and return to the previous one
        }
    }




    private fun setupRecyclerView() {
        val modelName = intent.getStringExtra("modelName") ?: "Unknown"
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""
        val brandId = intent.getStringExtra("brandId") ?: "" // Ensure you're passing this from the previous activity
        adapter = MotorTypeAdapter(mutableListOf(), brandId, modelCodigo, modelName)
        binding.recyclerViewModelMotors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewModelMotors.adapter = adapter
    }

    private fun fetchMotorTypes(marcaId: String, modeloId: String) {
        showLoading()
        Log.d("MotorTypesActivity", "fetchMotorTypes called with marcaId: $marcaId, modeloId: $modeloId") // Add this line
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getMotorTypes(marcaId, modeloId).enqueue(object : Callback<List<MotorType>> {
            override fun onResponse(call: Call<List<MotorType>>, response: Response<List<MotorType>>) {
                if (response.isSuccessful) {
                    response.body()?.let { motorTypes ->
                        Log.d("MotorTypesActivity", "Fetched motor types: $motorTypes") // Log the fetched data
                        adapter.updateData(motorTypes)
                        showContent()
                    }
                } else {
                    showError()
                    Log.e("MotorTypesActivity", "Failed to fetch motor types. Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MotorType>>, t: Throwable) {
                showError()
                Log.e("MotorTypesActivity", "Error fetching motor types: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE // Show the loading text
        binding.recyclerViewModelMotors.visibility = View.GONE
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
        binding.recyclerViewModelMotors.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.loadingTextView.visibility = View.GONE
    }


}
