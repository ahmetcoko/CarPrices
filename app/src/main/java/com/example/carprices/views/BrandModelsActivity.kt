package com.example.carprices.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carprices.adapter.CarModelAdapter
import com.example.carprices.network.CarApiService
import com.example.carprices.model.CarModelsResponse
import com.example.carprices.network.RetrofitClient
import com.example.carprices.databinding.ActivityBrandModelsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrandModelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrandModelsBinding
    private lateinit var adapter: CarModelAdapter
    private var brandCodigo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandModelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val brandName = intent.getStringExtra("brandName") ?: ""
        brandCodigo = intent.getStringExtra("brandCodigo") ?: ""

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

        binding.retryButton.setOnClickListener {
            fetchCarModels(brandCodigo)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = CarModelAdapter(mutableListOf(), brandCodigo)
        binding.recyclerViewCarModels.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCarModels.adapter = adapter
    }

    private fun fetchCarModels(brandCodigo: String) {
        showLoading()
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getCarModels(brandCodigo).enqueue(object : Callback<CarModelsResponse> {
            override fun onResponse(call: Call<CarModelsResponse>, response: Response<CarModelsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { modelsResponse ->
                        adapter.updateData(modelsResponse.modelos)
                        showContent()
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<CarModelsResponse>, t: Throwable) {
                showError()
                t.printStackTrace()
            }
        })
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE
        binding.recyclerViewCarModels.visibility = View.GONE
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
        binding.recyclerViewCarModels.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.loadingTextView.visibility = View.GONE
    }
}

