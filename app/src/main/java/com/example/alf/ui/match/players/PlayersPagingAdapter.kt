package com.example.alf.ui.match.players;

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
import com.example.alf.data.model.Player
import com.example.alf.databinding.ItemPlayerBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class PlayersPagingAdapter(
        diffCallback: DiffUtil.ItemCallback<Player>,
        private var listener: PlayerListener
        ): PagingDataAdapter<Player, PlayersPagingAdapter.ViewHolder>(diffCallback) {

    companion object {

        private val dateFormat = SimpleDateFormat(
                AlfApplication.getProperty("dateFormat"),
                Locale.getDefault()
        )

        private fun buildPlayerPhotoUrl(player: Player): String {
            return AlfApplication.getProperty("url.image.player") +
                    player.id +
                    AlfApplication.getProperty("extension.image.player")
        }

        private fun buildFlagImageUrl(player: Player): String {
            return AlfApplication.getProperty("url.image.flag") +
                    player.country?.name?.toLowerCase(Locale.ROOT) +
                    AlfApplication.getProperty("extension.image.flag")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPlayerPhoto(imageView: ImageView, player: Player) {
            val url = buildPlayerPhotoUrl(player)
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
        fun loadFlagImage(imageView: ImageView, player: Player) {
            val url = buildFlagImageUrl(player)
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

    interface PlayerListener {
        fun onItemClick(player: Player)
    }

    inner class ViewHolder(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.player = player
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = getItem(position)
        if (player != null) {
            holder.bind(player)
            holder.itemView.setOnClickListener { listener.onItemClick(player) }
        }
    }

    object PlayerComparator : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }
}