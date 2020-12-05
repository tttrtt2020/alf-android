package com.example.alf.ui.match.events.live;

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.AlfApplication
import com.example.alf.data.model.event.LiveEventType
import com.example.alf.databinding.ItemLiveEventTypeBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class LiveEventTypesAdapter(var listener: LiveEventTypesListener) :
        RecyclerView.Adapter<LiveEventTypesAdapter.ViewHolder>() {

    companion object {

        private fun buildEventTypeIconUrl(liveEventType: LiveEventType): String {
            return AlfApplication.getProperty("url.icon.event") +
                    liveEventType.id +
                    AlfApplication.getProperty("extension.icon.event")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadEventIcon(imageView: ImageView, liveEventType: LiveEventType) {
            val url = buildEventTypeIconUrl(liveEventType)
            if (url.isNotEmpty()) {
                GlideToVectorYou.init().with(imageView.context).load(
                        Uri.parse(url),
                        imageView
                )
            }
        }
    }

    private var liveEventTypes: List<LiveEventType> = ArrayList()

    interface LiveEventTypesListener {
        fun onItemClick(liveEventType: LiveEventType)
    }

    fun setLiveEventTypes(list: List<LiveEventType>) {
        liveEventTypes = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemLiveEventTypeBinding) : RecyclerView.ViewHolder(binding.root) {

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
            listener.onItemClick(liveEventType)
        }
    }

    override fun getItemCount() = liveEventTypes.size

}