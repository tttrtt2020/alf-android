package com.example.alf.ui.match.squad;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.databinding.ItemMatchPlayerBinding

class MatchPlayersAdapter(
        private var matchPlayers: List<MatchPlayer>,
        private var listener: SquadListener
        ) :
        RecyclerView.Adapter<MatchPlayersAdapter.ViewHolder>() {

    companion object {

        private fun buildPersonPhotoUrl(person: Player): String {
            return AlfApplication.getProperty("url.image.person") +
                    person.id +
                    AlfApplication.getProperty("extension.image.person")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPersonPhoto(imageView: ImageView, person: Player) {
            val url = buildPersonPhotoUrl(person)
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
        @BindingAdapter("android:src")
        fun setBackground(imageView: ImageView, inStart: Boolean) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                            imageView.context,
                            if (inStart) R.drawable.ic_run else R.drawable.ic_seat
                    )
            )
        }
    }

    interface SquadListener {
        fun onItemDeleted(matchPlayer: MatchPlayer, position: Int)

        fun onItemClick(matchPlayer: MatchPlayer)

        fun onItemLongClick(view: View, matchPlayer: MatchPlayer, position: Int): Boolean
    }

    fun setMatchPlayers(list: List<MatchPlayer>) {
        matchPlayers = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemMatchPlayerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(matchPlayer: MatchPlayer) {
            binding.matchPlayer = matchPlayer

            // todo: choose correct logic
            binding.inStart = matchPlayer.fieldPosition != null
            //binding.inStart = matchPlayer.timeIn == 0

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchPlayerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matchPlayer = matchPlayers[position]
        holder.bind(matchPlayer)
        holder.itemView.setOnClickListener { listener.onItemClick(matchPlayer) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, matchPlayer, position) }
    }

    override fun getItemCount() = matchPlayers.size

}