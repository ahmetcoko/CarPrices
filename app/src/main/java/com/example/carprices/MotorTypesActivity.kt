package com.example.carprices

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.databinding.ActivityMotorTypesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MotorTypesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMotorTypesBinding
    private lateinit var adapter: MotorTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotorTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val modelName = intent.getStringExtra("modelName") ?: "Unknown"
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""

        binding.ModelMotorsTitleTextView.text = modelName

        setupRecyclerView()
        if (modelCodigo.isNotEmpty()) {
            fetchMotorTypes(modelCodigo)
        }
    }

    private fun setupRecyclerView() {
        adapter = MotorTypeAdapter(mutableListOf())
        binding.recyclerViewModelMotors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewModelMotors.adapter = adapter
    }

    private fun fetchMotorTypes(modelCodigo: String) {
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getMotorTypes("specificMarcaId", modelCodigo).enqueue(object :
            Callback<List<MotorType>> {
            override fun onResponse(call: Call<List<MotorType>>, response: Response<List<MotorType>>) {
                if (response.isSuccessful) {
                    response.body()?.let { motorTypes ->
                        adapter.updateData(motorTypes)
                    }
                } else {
                    Log.e("MotorTypesActivity", "Failed to fetch motor types. Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MotorType>>, t: Throwable) {
                Log.e("MotorTypesActivity", "Error fetching motor types: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
