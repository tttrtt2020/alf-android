package com.example.alf.ui.matches;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Match
import java.text.SimpleDateFormat
import java.util.*

class MatchesPagingAdapter(diffCallback: DiffUtil.ItemCallback<Match>, var listener: MatchListener) :
    PagingDataAdapter<Match, MatchesPagingAdapter.ViewHolder>(diffCallback) {

    interface MatchListener {
        fun onItemDeleted(match: Match, position: Int)

        fun onItemClick(match: Match, position: Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hostLogoImageView: ImageView? = null
        var guestLogoImageView: ImageView? = null
        var hostNameTextView: TextView? = null
        var guestNameTextView: TextView? = null
        var resultTextView: TextView? = null
        var dateTextView: TextView? = null
        var timeTextView: TextView? = null

        init {
            hostLogoImageView = itemView.findViewById(R.id.host_logo)
            guestLogoImageView = itemView.findViewById(R.id.guest_logo)
            hostNameTextView = itemView.findViewById(R.id.host_name)
            guestNameTextView = itemView.findViewById(R.id.guest_name)
            resultTextView = itemView.findViewById(R.id.result)
            dateTextView = itemView.findViewById(R.id.date)
            timeTextView = itemView.findViewById(R.id.time)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_match, parent, false) as View
        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val match = getItem(position)

        if (match != null) {
            holder.hostNameTextView?.text = match.hostMatchTeam.team.name
            holder.guestNameTextView?.text = match.guestMatchTeam.team.name
            holder.resultTextView?.text = if (match.status.equals("FINISHED"))
                (match.resultHostGoals.toString() + ":" + match.resultGuestGoals.toString()) else "- : -"
            holder.dateTextView?.text = if (match.dateTime == null) "-" else
                SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(match.dateTime)
            holder.timeTextView?.text = if (match.dateTime == null) "-" else
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(match.dateTime)
            // load logos
            val hostLogoImageUrl = AlfApplication.getProperty("url.image.club") +
                    match.hostMatchTeam.team.club.id +
                    AlfApplication.getProperty("extension.club")
            holder.hostLogoImageView?.context?.let {
                Glide
                    .with(it)
                    .load(hostLogoImageUrl)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(holder.hostLogoImageView!!)
            }
            val guestLogoImageUrl = AlfApplication.getProperty("url.image.club") +
                    match.guestMatchTeam.team.club.id +
                    AlfApplication.getProperty("extension.club")
            holder.guestLogoImageView?.context?.let {
                Glide
                    .with(it)
                    .load(guestLogoImageUrl)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(holder.guestLogoImageView!!)
            }

            holder.itemView.setOnClickListener {
                listener.onItemClick(match, position)
            }
        }

    }

    object MatchModelComparator : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }
    }
}