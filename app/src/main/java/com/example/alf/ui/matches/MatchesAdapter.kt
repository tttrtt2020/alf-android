package com.example.alf.ui.matches;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.data.model.MatchModel
import java.text.SimpleDateFormat
import java.util.*

class MatchesAdapter(var listener: MatchListener) :
        RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

    companion object {
        const val clubLogosUrl: String = "https://storage.googleapis.com/alf-dev/club/"
        const val clubLogosExtension: String = ".png"
    }

    private var matches: ArrayList<MatchModel>? = null

    interface MatchListener {
        fun onItemDeleted(matchModel: MatchModel, position: Int)

        fun onItemClick(matchModel: MatchModel, position: Int)
    }

    fun setMatches(list: ArrayList<MatchModel>) {
        matches = list
        notifyDataSetChanged()
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
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_match, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        //val nameTextView = itemView.findViewById<TextView>(R.id.match_name)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val match = matches?.get(position)
        holder.hostNameTextView?.text = match?.hostMatchTeam?.team?.name
        holder.guestNameTextView?.text = match?.guestMatchTeam?.team?.name
        holder.resultTextView?.text = if (match?.status.equals("FINISHED"))
            (match?.resultHostGoals.toString() + ":" + match?.resultGuestGoals.toString()) else "- : -"
        holder.dateTextView?.text = if (match?.dateTime == null) "-" else
            SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(match.dateTime)
        holder.dateTextView?.text = if (match?.dateTime == null) "-" else
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(match.dateTime)
        // load logos
        val hostLogoImageUrl = clubLogosUrl + match?.hostMatchTeam?.team?.clubId + clubLogosExtension
        holder.hostLogoImageView?.context?.let {
            Glide
                .with(it)
                .load(hostLogoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.hostLogoImageView!!)
        }
        val guestLogoImageUrl = clubLogosUrl + match?.guestMatchTeam?.team?.clubId + clubLogosExtension
        holder.guestLogoImageView?.context?.let {
            Glide
                .with(it)
                .load(guestLogoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.guestLogoImageView!!)
        }

        holder.itemView.setOnClickListener {
            if (match != null) {
                listener.onItemClick(match, position)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = matches?.size ?: 0

}