package com.example.alf.ui.match.statuses;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.match.Status
import com.example.alf.databinding.ItemStatusBinding

class StatusesAdapter(
        private var statuses: List<Status>,
        private var listener: StatusesListener
        ) :
        RecyclerView.Adapter<StatusesAdapter.ViewHolder>() {

    interface StatusesListener {
        fun onItemClick(status: Status)
    }

    fun setStatuses(list: List<Status>) {
        statuses = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(status: Status) {
            binding.status = status
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStatusBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = statuses[position]

        holder.bind(status)
        holder.itemView.setOnClickListener {
            listener.onItemClick(status)
        }
    }

    override fun getItemCount() = statuses.size

}