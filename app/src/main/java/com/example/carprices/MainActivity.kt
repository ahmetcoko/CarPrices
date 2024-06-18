package com.example.carprices

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CarBrandAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the RecyclerView with a LinearLayoutManager
        binding.recyclerViewCarBrands.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty mutable list
        adapter = CarBrandAdapter(mutableListOf())
        binding.recyclerViewCarBrands.adapter = adapter

        // Setup network call to fetch car brands
        fetchCarBrands()
    }

    private fun fetchCarBrands() {
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getCarBrands().enqueue(object : Callback<List<CarBrand>> {
            override fun onResponse(call: Call<List<CarBrand>>, response: Response<List<CarBrand>>) {
                if (response.isSuccessful) {
                    response.body()?.let { brands ->
                        // Update the adapter with the received brands
                        adapter.updateData(brands)
                    }
                } else {
                    Log.e("MainActivity", "Failed to fetch brands. Response: $response")
                }
            }

            override fun onFailure(call: Call<List<CarBrand>>, t: Throwable) {
                // Handle network errors
                Log.e("MainActivity", "Error fetching brands: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
