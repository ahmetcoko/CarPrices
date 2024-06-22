package com.example.carprices.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarModelBinding
import java.util.Locale
import android.widget.Filter
import android.widget.Filterable
import com.example.carprices.model.CarModel
import com.example.carprices.views.MotorTypesActivity

class CarModelAdapter(private val carModels: MutableList<CarModel>, private val brandId: String) : RecyclerView.Adapter<CarModelAdapter.CarModelViewHolder>(), Filterable {
    var filteredCarModels: MutableList<CarModel> = carModels.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarModelBinding.inflate(inflater, parent, false)
        return CarModelViewHolder(binding, brandId)
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        val model = filteredCarModels[position]
        holder.binding.CarModelName.text = model.nome
        holder.bind(model)  // Use the bind function correctly
    }

    override fun getItemCount(): Int = filteredCarModels.size

    class CarModelViewHolder(val binding: ItemCarModelBinding, private val brandId: String) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CarModel) {
            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, MotorTypesActivity::class.java).apply {
                    putExtra("modelName", model.nome)
                    putExtra("modelCodigo", model.codigo)
                    putExtra("brandId", brandId)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charString = constraint?.toString() ?: ""
            filteredCarModels = if (charString.isEmpty()) {
                carModels
            } else {
                carModels.filter { it.nome.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT)) }.toMutableList()
            }
            return FilterResults().apply { values = filteredCarModels }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredCarModels = results?.values as MutableList<CarModel>? ?: mutableListOf()
            notifyDataSetChanged()
        }
    }

    fun updateData(newCarModels: List<CarModel>) {
        carModels.clear()
        carModels.addAll(newCarModels)
        filteredCarModels = newCarModels.toMutableList()
        notifyDataSetChanged()
    }
}

