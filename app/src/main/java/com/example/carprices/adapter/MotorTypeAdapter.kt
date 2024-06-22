package com.example.carprices.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.model.MotorType
import com.example.carprices.views.PricesActivity
import com.example.carprices.databinding.ItemCarMotorBinding

class MotorTypeAdapter(private val motorTypes: MutableList<MotorType>, private val brandId: String, private val modelCodigo: String, private val modelName: String) : RecyclerView.Adapter<MotorTypeAdapter.MotorTypeViewHolder>() {

    class MotorTypeViewHolder(val binding: ItemCarMotorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorTypeViewHolder {
        val binding = ItemCarMotorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MotorTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MotorTypeViewHolder, position: Int) {
        holder.binding.ModelMotorName.text = motorTypes[position].nome
        holder.binding.root.setOnClickListener {
            val context = it.context
            val intent = Intent(context, PricesActivity::class.java)
            intent.putExtra("brandId", brandId)
            intent.putExtra("modelCodigo", modelCodigo)
            intent.putExtra("modelName", modelName)
            // Passing the 'codigo' instead of 'nome'
            intent.putExtra("selectedMotorType", motorTypes[position].codigo)
            context.startActivity(intent)
        }
    }


    override fun getItemCount() = motorTypes.size

    fun updateData(newMotorTypes: List<MotorType>) {
        motorTypes.clear()
        motorTypes.addAll(newMotorTypes)
        notifyDataSetChanged()
    }
}
