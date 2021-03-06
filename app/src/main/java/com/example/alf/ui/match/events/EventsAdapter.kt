package com.example.alf.ui.match.events;

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alf.AlfApplication
import com.example.alf.data.model.event.Event
import com.example.alf.data.model.event.EventType
import com.example.alf.databinding.ItemEventBinding
import com.example.alf.ui.toPx
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


class EventsAdapter(
        events: List<Event>,
        private var hostTeamId: Int,
        private var guestTeamId: Int,
        private var listener: EventsListener
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    companion object {

        private fun buildEventTypeIconUrl(eventType: EventType): String {
            return AlfApplication.getProperty("url.icon.event") +
                    eventType.id +
                    AlfApplication.getProperty("extension.icon.event")
        }

        @JvmStatic
        @BindingAdapter("app:imageUrl")
        fun loadEventIcon(imageView: ImageView, event: Event) {
            val url = buildEventTypeIconUrl(event.eventType)
            if (url.isNotEmpty()) {
                GlideToVectorYou.init().with(imageView.context).load(
                        Uri.parse(url),
                        imageView
                )
            }
        }

        @JvmStatic
        @BindingAdapter("app:drawableUrl", "app:host")
        fun loadEventIcon(textView: AppCompatTextView, event: Event, host: Boolean) {
            val url = buildEventTypeIconUrl(event.eventType)
            if (url.isNotEmpty()) {
                // TODO: 1/28/21  fix icon size
                val size = 24.toPx
                GlideApp.with(textView.context)
                        .load(Uri.parse(url))
                        /*.placeholder(R.drawable.event_type)
                        .error(R.drawable.error_image)*/
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                val bitmap = (resource as PictureDrawable).toBitmap(resource.intrinsicWidth, resource.intrinsicHeight)
                                val d: Drawable = BitmapDrawable(textView.context.resources, Bitmap.createScaledBitmap(bitmap, size, size, true))
                                textView.setCompoundDrawablesWithIntrinsicBounds(if (host) d else null, null, if (host) null else d, null)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
            }
        }

    }

    private var events = if (events is ArrayList) events else ArrayList(events)

    fun setEvents(events: List<Event>) {
        this.events = if (events is ArrayList) events else ArrayList(events)
        notifyDataSetChanged()
    }

    fun removeEvent(position: Int, resultCallback: () -> Unit) {
        events.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, events.size)
        if (position == 0) resultCallback()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.itemView.setOnClickListener { listener.onItemClick(event) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, event, position) }
    }

    override fun getItemCount() = events.size

    inner class ViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.event = event
            binding.hostTeamId = hostTeamId
            binding.guestTeamId = guestTeamId
            binding.executePendingBindings()
        }
    }

    interface EventsListener {
        fun onItemClick(event: Event)

        fun onItemLongClick(view: View, event: Event, position: Int): Boolean

        fun onItemDeleted(event: Event, position: Int)
    }

}