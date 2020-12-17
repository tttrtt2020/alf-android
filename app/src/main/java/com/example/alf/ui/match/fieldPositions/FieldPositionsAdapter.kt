package com.example.alf.ui.match.fieldPositions;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.databinding.ItemFieldPositionBinding

class FieldPositionsAdapter(var listener: FieldPositionsListener) :
        RecyclerView.Adapter<FieldPositionsAdapter.ViewHolder>() {

    private var fieldPositions: ArrayList<FieldPosition> = ArrayList()

    interface FieldPositionsListener {
        fun onItemClick(fieldPosition: FieldPosition)
    }

    fun setFieldPositions(list: ArrayList<FieldPosition>) {
        fieldPositions = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFieldPositionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fieldPosition: FieldPosition) {
            binding.fieldPosition = fieldPosition
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFieldPositionBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fieldPosition = fieldPositions[position]

        holder.bind(fieldPosition)
        holder.itemView.setOnClickListener {
            listener.onItemClick(fieldPosition)
        }
    }

    override fun getItemCount() = fieldPositions.size

}