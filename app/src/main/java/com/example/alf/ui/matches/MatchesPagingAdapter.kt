package com.example.alf.ui.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.model.Team
import com.example.alf.databinding.ItemMatchBinding
import java.text.SimpleDateFormat
import java.util.*

class MatchesPagingAdapter(diffCallback: DiffUtil.ItemCallback<Match>, var listener: MatchListener) :
    PagingDataAdapter<Match, MatchesPagingAdapter.ViewHolder>(diffCallback) {

    private val dateFormat = SimpleDateFormat(AlfApplication.getProperty("matches.dateFormat"), Locale.getDefault())
    private val timeFormat = SimpleDateFormat(AlfApplication.getProperty("matches.timeFormat"), Locale.getDefault())

    interface MatchListener {
        fun onItemDeleted(match: Match, position: Int)

        fun onItemClick(match: Match)
    }

    class ViewHolder(private val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            @JvmStatic
            @BindingAdapter("app:imageUrl")
            fun loadTeamLogo(imageView: ImageView, url: String?) {
                if (!url.isNullOrEmpty()) {
                    Glide
                            .with(imageView.context)
                            .load(url)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.holo_red_dark)
                            .into(imageView)
                }
            }
        }

        fun bind(match: Match) {
            binding.match = match
            binding.adapter = bindingAdapter as MatchesPagingAdapter?
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = getItem(position)
        if (match != null) {
            holder.bind(match)
        }
    }

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatTime(date: Date): String {
        return timeFormat.format(date)
    }

    fun buildTeamLogoUrl(team: Team): String {
        return AlfApplication.getProperty("url.logo.club") +
                team.club.id +
                AlfApplication.getProperty("extension.logo.club")
    }

    fun onMatchClick(match: Match) {
        listener.onItemClick(match)
    }

    object MatchComparator : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }
    }
}