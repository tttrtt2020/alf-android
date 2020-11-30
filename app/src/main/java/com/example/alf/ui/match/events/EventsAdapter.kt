package com.example.alf.ui.match.events;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.ItemEventBinding

class EventsAdapter(var listener: EventsListener) :
        RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var events: List<Event> = ArrayList()

    interface EventsListener {
        fun onItemDeleted(event: Event, position: Int)

        fun onItemClick(event: Event)
    }

    fun setEvents(list: List<Event>) {
        events = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.itemView.setOnClickListener {
            listener.onItemClick(event)
        }
    }

    override fun getItemCount() = events.size

}