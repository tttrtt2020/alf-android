package com.example.alf.ui.matches

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
import com.example.alf.data.model.MatchListItem
import com.example.alf.data.model.Team
import com.example.alf.databinding.ItemMatchBinding
import java.text.SimpleDateFormat
import java.util.*

class MatchesPagingAdapter(diffCallback: DiffUtil.ItemCallback<MatchListItem>, var listener: MatchListener) :
    PagingDataAdapter<MatchListItem, MatchesPagingAdapter.ViewHolder>(diffCallback) {

    companion object {

        private val dateFormat = SimpleDateFormat(AlfApplication.getProperty("matches.dateFormat"), Locale.getDefault())
        private val timeFormat = SimpleDateFormat(AlfApplication.getProperty("matches.timeFormat"), Locale.getDefault())

        private fun buildTeamLogoUrl(team: Team): String {
            return AlfApplication.getProperty("url.logo.club") +
                    team.club.id +
                    AlfApplication.getProperty("extension.logo.club")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadTeamLogo(imageView: ImageView, team: Team) {
            val url = buildTeamLogoUrl(team)
            if (url.isNotEmpty()) {
                Glide
                        .with(imageView.context)
                        .load(url)
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.holo_red_dark)
                        .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:match_date")
        fun setDate(textView: TextView, dateTime: Date?) {
            textView.text = if (dateTime != null) dateFormat.format(dateTime) else ""
        }

        @JvmStatic
        @BindingAdapter("app:match_time")
        fun setTime(textView: TextView, dateTime: Date?) {
            textView.text = if (dateTime != null) timeFormat.format(dateTime) else ""
        }
    }

    interface MatchListener {
        fun onItemClick(match: MatchListItem)
    }

    inner class ViewHolder(private val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchListItem) {
            binding.match = match
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
            holder.itemView.setOnClickListener { listener.onItemClick(match) }
        }
    }

    object MatchComparator : DiffUtil.ItemCallback<MatchListItem>() {
        override fun areItemsTheSame(oldItem: MatchListItem, newItem: MatchListItem): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MatchListItem, newItem: MatchListItem): Boolean {
            return oldItem == newItem
        }
    }
}