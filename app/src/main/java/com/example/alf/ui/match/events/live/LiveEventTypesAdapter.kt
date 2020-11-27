package com.example.alf.ui.match.events.live;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.event.LiveEventType

class LiveEventTypesAdapter(var listener: LiveEventTypesListener) :
        RecyclerView.Adapter<LiveEventTypesAdapter.ViewHolder>() {

    private var liveEventTypes: List<LiveEventType>? = null

    interface LiveEventTypesListener {
        fun onItemClick(liveEventType: LiveEventType, position: Int)
    }

    fun setLiveEventTypes(list: List<LiveEventType>) {
        liveEventTypes = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var minuteTextView: TextView? = null
        var nameTextView: TextView? = null

        init {
            //minuteTextView = itemView.findViewById(R.id.minute)
            nameTextView = itemView.findViewById(R.id.name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_live_event_type, parent, false) as View

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val liveEventType = liveEventTypes?.get(position)

        holder.nameTextView?.text = liveEventType?.name

        holder.itemView.setOnClickListener {
            if (liveEventType != null) {
                listener.onItemClick(liveEventType, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = liveEventTypes?.size ?: 0

}