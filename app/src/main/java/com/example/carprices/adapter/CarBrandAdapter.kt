package com.example.carprices.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarBrandBinding
import android.widget.Filter
import android.widget.Filterable
import com.example.carprices.views.BrandModelsActivity
import com.example.carprices.model.CarBrand

class CarBrandAdapter(
    private var carBrands: List<CarBrand>
) : RecyclerView.Adapter<CarBrandAdapter.CarBrandViewHolder>(), Filterable {

    var filteredCarBrands: MutableList<CarBrand> = carBrands.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarBrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarBrandBinding.inflate(inflater, parent, false)
        return CarBrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarBrandViewHolder, position: Int) {
        val brand = filteredCarBrands[position]
        holder.binding.CarBrandName.text = brand.nome
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, BrandModelsActivity::class.java)
            intent.putExtra("brandName", brand.nome)
            intent.putExtra("brandCodigo", brand.codigo)  // Pass the brand code
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = filteredCarBrands.size

    class CarBrandViewHolder(val binding: ItemCarBrandBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newCarBrands: List<CarBrand>) {
        carBrands = newCarBrands
        filteredCarBrands = newCarBrands.toMutableList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                filteredCarBrands = if (charString.isEmpty()) {
                    carBrands.toMutableList()
                } else {
                    val filteredList = carBrands.filter {
                        it.nome.toLowerCase().contains(charString.toLowerCase())
                    }.toMutableList()
                    filteredList
                }
                return FilterResults().apply { values = filteredCarBrands }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCarBrands = results?.values as MutableList<CarBrand>
                notifyDataSetChanged()
            }
        }
    }
}





