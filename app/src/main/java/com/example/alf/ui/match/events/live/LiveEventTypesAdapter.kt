package com.example.alf.ui.match.events.live;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.event.LiveEventType
import com.example.alf.databinding.ItemLiveEventTypeBinding

class LiveEventTypesAdapter(var listener: LiveEventTypesListener) :
        RecyclerView.Adapter<LiveEventTypesAdapter.ViewHolder>() {

    private var liveEventTypes: List<LiveEventType> = ArrayList()

    interface LiveEventTypesListener {
        fun onItemClick(liveEventType: LiveEventType, position: Int)
    }

    fun setLiveEventTypes(list: List<LiveEventType>) {
        liveEventTypes = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemLiveEventTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(liveEventType: LiveEventType) {
            binding.liveEventType = liveEventType
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLiveEventTypeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val liveEventType = liveEventTypes[position]
        holder.bind(liveEventType)
        holder.itemView.setOnClickListener {
            listener.onItemClick(liveEventType, position)
        }
    }

    override fun getItemCount() = liveEventTypes.size

}