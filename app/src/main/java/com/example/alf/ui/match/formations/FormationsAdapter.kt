package com.example.alf.ui.match.formations;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.match.Formation
import com.example.alf.databinding.ItemFormationBinding

class FormationsAdapter(
        private var formations: List<Formation>,
        private var listener: FormationsListener
        ) :
        RecyclerView.Adapter<FormationsAdapter.ViewHolder>() {

    interface FormationsListener {
        fun onItemClick(formation: Formation)
    }

    fun setFormations(list: List<Formation>) {
        formations = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFormationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(formation: Formation) {
            binding.formation = formation
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFormationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formation = formations[position]

        holder.bind(formation)
        holder.itemView.setOnClickListener {
            listener.onItemClick(formation)
        }
    }

    override fun getItemCount() = formations.size

}