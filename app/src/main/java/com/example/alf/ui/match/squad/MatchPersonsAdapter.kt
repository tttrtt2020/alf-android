package com.example.alf.ui.match.squad;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.ui.persons.PersonsAdapter

class MatchPersonsAdapter(var listener: SquadListener) :
        RecyclerView.Adapter<MatchPersonsAdapter.ViewHolder>() {

    private var matchPersons: List<MatchPerson>? = null

    interface SquadListener {
        fun onItemDeleted(matchPerson: MatchPerson, position: Int)

        fun onItemClick(matchPerson: MatchPerson, position: Int)
    }

    fun setMatchPersons(list: List<MatchPerson>) {
        matchPersons = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoImageView: ImageView? = null
        var lastNameTextView: TextView? = null
        var firstNameTextView: TextView? = null
        var roleTextView: TextView? = null

        init {
            photoImageView = itemView.findViewById(R.id.photo)
            lastNameTextView = itemView.findViewById(R.id.last_name)
            firstNameTextView = itemView.findViewById(R.id.first_name)
            roleTextView = itemView.findViewById(R.id.role)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_match_person, parent, false) as View
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matchPerson = matchPersons?.get(position)

        holder.lastNameTextView?.text = matchPerson?.player?.lastName
        holder.firstNameTextView?.text = matchPerson?.player?.firstName

        // load photo
        val photoImageUrl = PersonsAdapter.personsImagesUrl + matchPerson?.player?.id + PersonsAdapter.personsImagesExtension
        holder.photoImageView?.context?.let {
            Glide
                .with(it)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.photoImageView!!)
        }

        holder.itemView.setOnClickListener {
            if (matchPerson != null) {
                listener.onItemClick(matchPerson, position)
            }
        }
    }

    override fun getItemCount() = matchPersons?.size ?: 0

}