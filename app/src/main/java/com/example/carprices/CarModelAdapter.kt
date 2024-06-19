package com.example.carprices

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarModelBinding

class CarModelAdapter(private val carModels: MutableList<CarModel>) : RecyclerView.Adapter<CarModelAdapter.CarModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarModelBinding.inflate(inflater, parent, false)
        return CarModelViewHolder(binding,carModels)
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        holder.binding.CarModelName.text = carModels[position].nome
        holder.bind(position)
    }

    override fun getItemCount(): Int = carModels.size

    class CarModelViewHolder(val binding: ItemCarModelBinding, private val carModels: MutableList<CarModel>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, MotorTypesActivity::class.java)
                intent.putExtra("modelName", carModels[position].nome)
                intent.putExtra("modelCodigo", carModels[position].codigo)
                // Add other fields as needed
                context.startActivity(intent)
            }
        }
    }

    fun updateData(newCarModels: List<CarModel>) {
        carModels.clear()
        carModels.addAll(newCarModels)
        notifyDataSetChanged()
    }
}
