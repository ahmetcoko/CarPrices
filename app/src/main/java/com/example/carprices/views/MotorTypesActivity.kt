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
        super.onCreate(savedInstanceState)
        binding = ActivityMotorTypesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val modelName = intent.getStringExtra("modelName") ?: "Unknown"
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""
        val brandId = intent.getStringExtra("brandId") ?: ""

        Log.d("MotorTypesActivity", "brandId: $brandId, modelCodigo: $modelCodigo")

        binding.ModelMotorsTitleTextView.text = modelName
        if (brandId.isNotEmpty() && modelCodigo.isNotEmpty()) {
            fetchMotorTypes(brandId, modelCodigo)
        }

        binding.retryButton.setOnClickListener {
            fetchMotorTypes(brandId, modelCodigo)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }




    private fun setupRecyclerView() {
        val modelName = intent.getStringExtra("modelName") ?: "Unknown"
        val modelCodigo = intent.getStringExtra("modelCodigo") ?: ""
        val brandId = intent.getStringExtra("brandId") ?: ""
        adapter = MotorTypeAdapter(mutableListOf(), brandId, modelCodigo, modelName)
        binding.recyclerViewModelMotors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewModelMotors.adapter = adapter
    }

    private fun fetchMotorTypes(marcaId: String, modeloId: String) {
        showLoading()
        val carService = RetrofitClient.instance.create(CarApiService::class.java)
        carService.getMotorTypes(marcaId, modeloId).enqueue(object : Callback<List<MotorType>> {
            override fun onResponse(call: Call<List<MotorType>>, response: Response<List<MotorType>>) {
                if (response.isSuccessful) {
                    response.body()?.let { motorTypes ->
                        adapter.updateData(motorTypes)
                        showContent()
                    }
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<List<MotorType>>, t: Throwable) {
                showError()
                t.printStackTrace()
            }
        })
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE
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
