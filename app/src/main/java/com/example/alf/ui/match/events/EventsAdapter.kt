package com.example.alf.ui.match.events;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.event.Event

class EventsAdapter(var listener: EventsListener) :
        RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var events: List<Event>? = null

    interface EventsListener {
        fun onItemDeleted(event: Event, position: Int)

        fun onItemClick(event: Event, position: Int)
    }

    fun setEvents(list: List<Event>) {
        events = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var minuteTextView: TextView? = null
        var nameTextView: TextView? = null

        init {
            minuteTextView = itemView.findViewById(R.id.minute)
            nameTextView = itemView.findViewById(R.id.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_event, parent, false) as View
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events?.get(position)

        holder.minuteTextView?.text = event?.minute
        holder.nameTextView?.text = event?.name

        holder.itemView.setOnClickListener {
            if (event != null) {
                listener.onItemClick(event, position)
            }
        }
    }

    override fun getItemCount() = events?.size ?: 0

}