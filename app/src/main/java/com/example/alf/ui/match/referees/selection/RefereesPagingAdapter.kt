package com.example.alf.ui.match.referees.selection;

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Referee
import com.example.alf.databinding.ItemRefereeBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class RefereesPagingAdapter(
        diffCallback: DiffUtil.ItemCallback<Referee>,
        private var listener: RefereeListener
        ): PagingDataAdapter<Referee, RefereesPagingAdapter.ViewHolder>(diffCallback) {

    companion object {

        private val dateFormat = SimpleDateFormat(
                AlfApplication.getProperty("dateFormat"),
                Locale.getDefault()
        )

        private fun buildRefereePhotoUrl(referee: Referee): String {
            return AlfApplication.getProperty("url.image.referee") +
                    referee.id +
                    AlfApplication.getProperty("extension.image.referee")
        }

        private fun buildFlagImageUrl(referee: Referee): String {
            return AlfApplication.getProperty("url.image.flag") +
                    referee.country?.name?.toLowerCase(Locale.ROOT) +
                    AlfApplication.getProperty("extension.image.flag")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadRefereePhoto(imageView: ImageView, referee: Referee) {
            val url = buildRefereePhotoUrl(referee)
            if (url.isNotEmpty()) {
                Glide
                        .with(imageView.context)
                        .load(url)
                        .placeholder(android.R.color.darker_gray)
                        .error(R.drawable.ic_no_photo_with_padding)
                        .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:vectorImageSrc")
        fun loadFlagImage(imageView: ImageView, referee: Referee) {
            val url = buildFlagImageUrl(referee)
            if (url.isNotEmpty()) {
                GlideToVectorYou.init().with(imageView.context).load(
                        Uri.parse(url),
                        imageView
                )
            }
        }

        @JvmStatic
        @BindingAdapter("app:date")
        fun setDate(textView: TextView, date: Date?) {
            textView.text = if (date != null) dateFormat.format(date) else ""
        }
    }

    interface RefereeListener {
        fun onItemClick(referee: Referee)
    }

    inner class ViewHolder(private val binding: ItemRefereeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(referee: Referee) {
            binding.referee = referee
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRefereeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val referee = getItem(position)
        if (referee != null) {
            holder.bind(referee)
            holder.itemView.setOnClickListener { listener.onItemClick(referee) }
        }
    }

    object RefereeComparator : DiffUtil.ItemCallback<Referee>() {
        override fun areItemsTheSame(oldItem: Referee, newItem: Referee): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Referee, newItem: Referee): Boolean {
            return oldItem == newItem
        }
    }
}