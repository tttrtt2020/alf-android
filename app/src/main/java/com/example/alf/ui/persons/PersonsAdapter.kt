package com.example.alf.ui.persons;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class PersonsAdapter(var listener: PersonListener) :
    RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    companion object {
        const val personsImagesUrl: String = "https://storage.googleapis.com/alf-dev/person/"
        const val personsImagesExtension: String = ".jpg"
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

        init {
            photoImageView = itemView.findViewById(R.id.photo)
            firstNameTextView = itemView.findViewById(R.id.first_name)
            patronymicTextView = itemView.findViewById(R.id.patronymic)
            lastNameTextView = itemView.findViewById(R.id.last_name)
            birthDateTextView = itemView.findViewById(R.id.birth_date)
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
        val imageUrl = personsImagesUrl + person?.id + personsImagesExtension
        val picasso: Picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso
            .load(imageUrl)
            .placeholder(R.color.white)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.photoImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = persons?.size ?: 0

}