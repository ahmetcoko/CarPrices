package com.example.carprices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carprices.databinding.ItemCarMotorBinding

class MotorTypeAdapter(private val motorTypes: MutableList<MotorType>) : RecyclerView.Adapter<MotorTypeAdapter.MotorTypeViewHolder>() {

    class MotorTypeViewHolder(val binding: ItemCarMotorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotorTypeViewHolder {
        val binding = ItemCarMotorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MotorTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MotorTypeViewHolder, position: Int) {
        holder.binding.ModelMotorName.text = motorTypes[position].nome
    }

    override fun getItemCount() = motorTypes.size

    fun updateData(newMotorTypes: List<MotorType>) {
        motorTypes.clear()
        motorTypes.addAll(newMotorTypes)
        notifyDataSetChanged()
    }
}
