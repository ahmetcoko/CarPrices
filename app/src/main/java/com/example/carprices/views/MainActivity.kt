package com.example.carprices.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.Adapter.CarBrandAdapter
import com.example.carprices.network.CarApiService
import com.example.carprices.network.RetrofitClient
import com.example.carprices.databinding.ActivityMainBinding
import com.example.carprices.model.CarBrand
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CarBrandAdapter
    private lateinit var allBrands: List<CarBrand>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewCarBrands.layoutManager = LinearLayoutManager(this)
        allBrands = mutableListOf()  // Initialize with empty list
        adapter = CarBrandAdapter(allBrands.toMutableList())
        binding.recyclerViewCarBrands.adapter = adapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }
        })

        binding.retryButton.setOnClickListener {
            fetchCarBrands()
        }

        fetchCarBrands()
    }


    private fun fetchCarBrands() {
        showLoading()
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getCarBrands().enqueue(object : Callback<List<CarBrand>> {
            override fun onResponse(call: Call<List<CarBrand>>, response: Response<List<CarBrand>>) {
                if (response.isSuccessful) {
                    response.body()?.let { brands ->
                        // Update the adapter with the received brands
                        adapter.updateData(brands)
                        showContent()
                    }
                } else {
                    showError()
                    Log.e("MainActivity", "Failed to fetch brands. Response: $response")
                }
            }

            override fun onFailure(call: Call<List<CarBrand>>, t: Throwable) {
                // Handle network errors
                showError()
                Log.e("MainActivity", "Error fetching brands: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE // Show the loading text
        binding.recyclerViewCarBrands.visibility = View.GONE
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
        binding.recyclerViewCarBrands.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.loadingTextView.visibility = View.GONE
    }

}
