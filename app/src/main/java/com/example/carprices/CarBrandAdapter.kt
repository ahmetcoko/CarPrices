package com.example.carprices

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarBrandBinding

class CarBrandAdapter(private var carBrands: MutableList<CarBrand>) : RecyclerView.Adapter<CarBrandAdapter.CarBrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarBrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarBrandBinding.inflate(inflater, parent, false)
        return CarBrandViewHolder(binding , carBrands)
    }

    override fun onBindViewHolder(holder: CarBrandViewHolder, position: Int) {
        holder.binding.CarBrandName.text = carBrands[position].nome
        holder.bind(position)
    }

    override fun getItemCount(): Int = carBrands.size

    class CarBrandViewHolder(val binding: ItemCarBrandBinding, private val carBrands: MutableList<CarBrand>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, BrandModelsActivity::class.java)
                intent.putExtra("brandName", carBrands[position].nome)
                intent.putExtra("brandCodigo", carBrands[position].codigo) // Pass the codigo
                context.startActivity(intent)
            }
        }
    }

    fun updateData(newCarBrands: List<CarBrand>) {
        carBrands.addAll(newCarBrands)
        notifyDataSetChanged()
    }
}




