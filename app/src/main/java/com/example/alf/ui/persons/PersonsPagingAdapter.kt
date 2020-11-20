package com.example.alf.ui.persons;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.data.model.Person
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class PersonsPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<Person>,
    private var listener: PersonListener
) :
    PagingDataAdapter<Person, PersonsPagingAdapter.ViewHolder>(diffCallback) {

    companion object {
        const val personsImagesUrl: String = "https://storage.googleapis.com/alf-dev/person/"
        const val flagsImagesUrl: String = "https://storage.googleapis.com/alf-dev/country/"
        const val personsImagesExtension: String = ".jpg"
        const val flagsImagesExtension: String = ".svg"
    }

    interface PersonListener {
        fun onItemDeleted(person: Person, position: Int)

        fun onItemClick(person: Person, position: Int)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_person, parent, false) as View
        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val person = getItem(position)
        holder.firstNameTextView?.text = person?.firstName
        holder.patronymicTextView?.text = person?.patronymic
        holder.lastNameTextView?.text = person?.lastName
        holder.birthDateTextView?.text = if (person?.birthDate == null) "" else
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(person.birthDate)
        // load photo
        val photoImageUrl = personsImagesUrl + person?.id + personsImagesExtension
        holder.photoImageView?.context?.let {
            Glide
                .with(it)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(R.drawable.ic_no_photo_with_padding)
                .into(holder.photoImageView!!)
        }
        // load flag
        val flagImageUrl =
            flagsImagesUrl + person?.country?.name?.toLowerCase(Locale.ROOT) + flagsImagesExtension
        GlideToVectorYou.init().with(holder.photoImageView?.context).load(
            Uri.parse(flagImageUrl),
            holder.flagImageView
        )

        holder.itemView.setOnClickListener {
            if (person != null) {
                listener.onItemClick(person, position)
            }
        }
    }

    object PersonComparator : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}