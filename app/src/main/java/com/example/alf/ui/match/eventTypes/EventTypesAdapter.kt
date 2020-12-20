package com.example.alf.ui.match.eventTypes;

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.AlfApplication
import com.example.alf.data.model.event.EventType
import com.example.alf.databinding.ItemEventTypeBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class EventTypesAdapter(var listener: EventTypesListener) :
        RecyclerView.Adapter<EventTypesAdapter.ViewHolder>() {

    companion object {

        private fun buildEventTypeIconUrl(eventType: EventType): String {
            return AlfApplication.getProperty("url.icon.event") +
                    eventType.id +
                    AlfApplication.getProperty("extension.icon.event")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadEventIcon(imageView: ImageView, eventType: EventType) {
            val url = buildEventTypeIconUrl(eventType)
            if (url.isNotEmpty()) {
                GlideToVectorYou.init().with(imageView.context).load(
                        Uri.parse(url),
                        imageView
                )
            }
        }
    }

    private var eventTypes: List<EventType> = ArrayList()

    interface EventTypesListener {
        fun onItemClick(eventType: EventType)
    }

    fun setEventTypes(list: List<EventType>) {
        eventTypes = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemEventTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(eventType: EventType) {
            binding.eventType = eventType
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEventTypeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventType = eventTypes[position]
        holder.bind(eventType)
        holder.itemView.setOnClickListener {
            listener.onItemClick(eventType)
        }
    }

    override fun getItemCount() = eventTypes.size

}