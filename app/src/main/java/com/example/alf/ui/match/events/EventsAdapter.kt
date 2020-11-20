package com.example.alf.ui.match.events;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.Event
import java.util.*

class EventsAdapter(var listener: EventsListener) :
        RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var events: ArrayList<Event>? = null

    interface EventsListener {
        fun onItemDeleted(event: Event, position: Int)

        fun onItemClick(event: Event, position: Int)
    }

    fun setEvents(list: ArrayList<Event>) {
        events = list
        notifyDataSetChanged()
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var minuteTextView: TextView? = null
        var nameTextView: TextView? = null

        init {
            minuteTextView = itemView.findViewById(R.id.minute)
            nameTextView = itemView.findViewById(R.id.name)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_event, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        //val nameTextView = itemView.findViewById<TextView>(R.id.match_name)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val event = events?.get(position)

        holder.minuteTextView?.text = event?.minute
        holder.nameTextView?.text = event?.name

        holder.itemView.setOnClickListener {
            if (event != null) {
                listener.onItemClick(event, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = events?.size ?: 0

}