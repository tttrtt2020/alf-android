package com.example.alf.ui.persons;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class PersonsAdapter(var listener: PersonListener) :
    RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    companion object {
        const val personsImagesUrl: String = "https://storage.googleapis.com/alf-dev/person/"
        const val flagsImagesUrl: String = "https://storage.googleapis.com/alf-dev/country/"
        const val personsImagesExtension: String = ".jpg"
        const val flagsImagesExtension: String = ".svg"
    }

    private var persons: ArrayList<PersonModel>? = null

    interface PersonListener {
        fun onItemDeleted(personModel: PersonModel, position: Int)
    }

    fun setPersons(list: ArrayList<PersonModel>) {
        persons = list
        notifyDataSetChanged()
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoImageView: ImageView? = null
        var firstNameTextView: TextView? = null
        var patronymicTextView: TextView? = null
        var lastNameTextView: TextView? = null
        var birthDateTextView: TextView? = null
        var flagImageView: ImageView? = null

        init {
            photoImageView = itemView.findViewById(R.id.photo)
            firstNameTextView = itemView.findViewById(R.id.first_name)
            patronymicTextView = itemView.findViewById(R.id.patronymic)
            lastNameTextView = itemView.findViewById(R.id.last_name)
            birthDateTextView = itemView.findViewById(R.id.birth_date)
            flagImageView = itemView.findViewById(R.id.flag)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonsAdapter.ViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_person, parent, false) as View
        // set the view's size, margins, paddings and layout parameters
        //val nameTextView = itemView.findViewById<TextView>(R.id.person_name)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val person = persons?.get(position)
        holder.firstNameTextView?.text = person?.firstName
        holder.patronymicTextView?.text = person?.patronymic
        holder.lastNameTextView?.text = person?.lastName
        holder.birthDateTextView?.text = if (person?.birthDate == null) "" else
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(person.birthDate)
        // load photo
        val photoImageUrl = personsImagesUrl + person?.id + personsImagesExtension
        holder.photoImageView?.context?.let {
            Glide
                .with(it)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.photoImageView!!)
        };
        // load flag
        val flagImageUrl = flagsImagesUrl + person?.country?.name?.toLowerCase(Locale.ROOT) + flagsImagesExtension
        GlideToVectorYou.init().with(holder.photoImageView?.context).load(Uri.parse(flagImageUrl), holder.flagImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = persons?.size ?: 0

}