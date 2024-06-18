package com.example.carprices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarBrandBinding

class CarBrandAdapter(private var carBrands: MutableList<CarBrand>) : RecyclerView.Adapter<CarBrandAdapter.CarBrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarBrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarBrandBinding.inflate(inflater, parent, false)
        return CarBrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarBrandViewHolder, position: Int) {
        holder.binding.CarBrandName.text = carBrands[position].nome
    }

    override fun getItemCount(): Int = carBrands.size

    class CarBrandViewHolder(val binding: ItemCarBrandBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newCarBrands: List<CarBrand>) {
        carBrands.addAll(newCarBrands)
        notifyDataSetChanged()
    }
}




