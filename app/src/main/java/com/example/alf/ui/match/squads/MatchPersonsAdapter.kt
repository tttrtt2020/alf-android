package com.example.alf.ui.match.squads;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.data.model.match.MatchPersonModel
import com.example.alf.ui.persons.PersonsAdapter
import java.util.*

class MatchPersonsAdapter(var listener: SquadsListener) :
        RecyclerView.Adapter<MatchPersonsAdapter.ViewHolder>() {

    private var matchPersons: ArrayList<MatchPersonModel>? = null

    interface SquadsListener {
        fun onItemDeleted(matchPersonModel: MatchPersonModel, position: Int)

        fun onItemClick(matchPersonModel: MatchPersonModel, position: Int)
    }

    fun setMatchPersons(list: ArrayList<MatchPersonModel>) {
        matchPersons = list
        notifyDataSetChanged()
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
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


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_match_person, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        //val nameTextView = itemView.findViewById<TextView>(R.id.match_name)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = matchPersons?.size ?: 0

}