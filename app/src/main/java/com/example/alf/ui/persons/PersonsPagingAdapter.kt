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
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Person
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class PersonsPagingAdapter(
        diffCallback: DiffUtil.ItemCallback<Person>,
        private var listener: PersonListener
        ): PagingDataAdapter<Person, PersonsPagingAdapter.ViewHolder>(diffCallback) {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat(
        AlfApplication.getProperty("dateFormat"),
        Locale.getDefault()
    )

    interface PersonListener {
        fun onItemDeleted(person: Person, position: Int)

        fun onItemClick(person: Person, position: Int)
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_person, parent, false) as View
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        holder.firstNameTextView?.text = person?.firstName
        holder.patronymicTextView?.text = person?.patronymic
        holder.lastNameTextView?.text = person?.lastName
        holder.birthDateTextView?.text = if (person?.birthDate == null)
            "" else dateFormat.format(person.birthDate)
        // load photo
        val photoImageUrl = AlfApplication.getProperty("url.image.person") + person?.id + AlfApplication.getProperty("extension.image.person")
        holder.photoImageView?.context?.let {
            Glide
                .with(it)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(R.drawable.ic_no_photo_with_padding)
                .into(holder.photoImageView!!)
        }
        // load flag
        val flagImageUrl = AlfApplication.getProperty("url.image.flag") +
                person?.country?.name?.toLowerCase(Locale.ROOT) +
                AlfApplication.getProperty("extension.image.flag")
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