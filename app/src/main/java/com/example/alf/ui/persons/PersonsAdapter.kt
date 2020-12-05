package com.example.alf.ui.persons;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Person
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

    private var persons: List<Person>? = null

    private val dateFormat: SimpleDateFormat = SimpleDateFormat(
        AlfApplication.getProperty("dateFormat"),
        Locale.getDefault()
    )

    interface PersonListener {
        fun onItemDeleted(person: Person, position: Int)

        fun onItemClick(person: Person, position: Int)
    }

    fun setPersons(list: List<Person>) {
        persons = list
        notifyDataSetChanged()
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
            .inflate(R.layout.item_person, parent, false) as View // todo: should use PersonsAdapter!
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = persons?.get(position)
        holder.firstNameTextView?.text = person?.firstName
        holder.patronymicTextView?.text = person?.patronymic
        holder.lastNameTextView?.text = person?.lastName
        holder.birthDateTextView?.text = if (person?.birthDate == null)
            "" else dateFormat.format(person.birthDate)
        // load photo
        val photoImageUrl = personsImagesUrl + person?.id + personsImagesExtension
        holder.photoImageView?.context?.let {
            Glide
                .with(it)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(holder.photoImageView!!)
        }
        // load flag
        val flagImageUrl = flagsImagesUrl + person?.country?.name?.toLowerCase(Locale.ROOT) + flagsImagesExtension
        GlideToVectorYou.init().with(holder.photoImageView?.context).load(Uri.parse(flagImageUrl), holder.flagImageView)

        holder.itemView.setOnClickListener {
            if (person != null) {
                listener.onItemClick(person, position)
            }
        }
    }

    override fun getItemCount() = persons?.size ?: 0

}