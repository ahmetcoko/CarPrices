package com.example.carprices

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.databinding.ActivityBrandModelsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrandModelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrandModelsBinding
    private lateinit var adapter: CarModelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandModelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val brandName = intent.getStringExtra("brandName") ?: ""
        val brandCodigo = intent.getStringExtra("brandCodigo") ?: ""

        binding.BrandModelsTitleTextView.text = brandName
        setupRecyclerView()
        fetchCarModels(brandCodigo)

        binding.searchModelEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }
        })
    }


    private fun setupRecyclerView() {
        val brandCodigo = intent.getStringExtra("brandCodigo") ?: ""
        adapter = CarModelAdapter(mutableListOf(), brandCodigo) // Pass the brandId to the adapter
        binding.recyclerViewCarModels.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCarModels.adapter = adapter
    }


    private fun fetchCarModels(brandCodigo: String) {
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getCarModels(brandCodigo).enqueue(object : Callback<CarModelsResponse> {
            override fun onResponse(call: Call<CarModelsResponse>, response: Response<CarModelsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { modelsResponse ->
                        adapter.updateData(modelsResponse.modelos)
                    }
                } else {
                    Log.e("BrandModelsActivity", "Failed to fetch models. Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CarModelsResponse>, t: Throwable) {
                Log.e("BrandModelsActivity", "Error fetching models: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}

