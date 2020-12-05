package com.example.alf.ui.persons;

import android.net.Uri
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
import com.example.alf.R
import com.example.alf.data.model.Person
import com.example.alf.databinding.ItemPersonBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import java.text.SimpleDateFormat
import java.util.*


class PersonsPagingAdapter(
        diffCallback: DiffUtil.ItemCallback<Person>,
        private var listener: PersonListener
        ): PagingDataAdapter<Person, PersonsPagingAdapter.ViewHolder>(diffCallback) {

    companion object {

        private val dateFormat = SimpleDateFormat(
                AlfApplication.getProperty("dateFormat"),
                Locale.getDefault()
        )

        private fun buildPersonPhotoUrl(person: Person): String {
            return AlfApplication.getProperty("url.image.person") +
                    person.id +
                    AlfApplication.getProperty("extension.image.person")
        }

        private fun buildFlagImageUrl(person: Person): String {
            return AlfApplication.getProperty("url.image.flag") +
                    person.country?.name?.toLowerCase(Locale.ROOT) +
                    AlfApplication.getProperty("extension.image.flag")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPersonPhoto(imageView: ImageView, person: Person) {
            val url = buildPersonPhotoUrl(person)
            if (url.isNotEmpty()) {
                Glide
                        .with(imageView.context)
                        .load(url)
                        .placeholder(android.R.color.darker_gray)
                        .error(R.drawable.ic_no_photo_with_padding)
                        .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:vectorImageSrc")
        fun loadFlagImage(imageView: ImageView, person: Person) {
            val url = buildFlagImageUrl(person)
            if (url.isNotEmpty()) {
                GlideToVectorYou.init().with(imageView.context).load(
                        Uri.parse(url),
                        imageView
                )
            }
        }

        @JvmStatic
        @BindingAdapter("app:date")
        fun setDate(textView: TextView, date: Date?) {
            textView.text = if (date != null) dateFormat.format(date) else ""
        }
    }

    interface PersonListener {
        fun onItemDeleted(person: Person, position: Int)

        fun onItemClick(person: Person)
    }

    inner class ViewHolder(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {
            binding.person = person
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        if (person != null) {
            holder.bind(person)
            holder.itemView.setOnClickListener { listener.onItemClick(person) }
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