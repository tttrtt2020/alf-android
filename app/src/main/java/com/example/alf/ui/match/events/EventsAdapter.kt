package com.example.alf.ui.match.events;

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.AlfApplication
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.ItemEventBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

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

        companion object {
            @JvmStatic
            @BindingAdapter("app:imageUrl")
            fun loadEventIcon(imageView: ImageView, url: String?) {
                if (!url.isNullOrEmpty()) {
                    GlideToVectorYou.init().with(imageView.context).load(
                            Uri.parse(url),
                            imageView
                    )
                }
            }
        }

        fun bind(event: Event) {
            binding.event = event
            binding.adapter = bindingAdapter as EventsAdapter?
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

    fun buildEventIconUrl(event: Event): String {
        return AlfApplication.getProperty("url.icon.event") +
                event.eventType.id +
                AlfApplication.getProperty("extension.icon.event")
    }

    override fun getItemCount() = events.size

}