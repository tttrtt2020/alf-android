package com.example.alf.ui.match.squad;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Format
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.databinding.ItemMatchPersonBinding

class MatchPersonsAdapter(var listener: SquadListener, val format: Format) :
        RecyclerView.Adapter<MatchPersonsAdapter.ViewHolder>() {

    companion object {

        private fun buildPersonPhotoUrl(person: Player): String {
            return AlfApplication.getProperty("url.image.person") +
                    person.id +
                    AlfApplication.getProperty("extension.image.person")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPersonPhoto(imageView: ImageView, person: Player) {
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
        @BindingAdapter("android:src")
        fun setBackground(imageView: ImageView, inStart: Boolean) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                            imageView.context,
                            if (inStart) R.drawable.ic_run else R.drawable.ic_seat
                    )
            )
        }
    }

    private var matchPersons: List<MatchPerson> = ArrayList()

    interface SquadListener {
        fun onItemDeleted(matchPerson: MatchPerson, position: Int)

        fun onItemClick(matchPerson: MatchPerson)

        fun onItemLongClick(view: View, matchPerson: MatchPerson, position: Int): Boolean
    }

    fun setMatchPersons(list: List<MatchPerson>) {
        matchPersons = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemMatchPersonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(matchPerson: MatchPerson) {
            binding.matchPerson = matchPerson
            binding.inStart = matchPerson.timeIn == 0
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchPersonBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matchPerson = matchPersons[position]
        holder.bind(matchPerson)
        holder.itemView.setOnClickListener { listener.onItemClick(matchPerson) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, matchPerson, position) }
    }

    override fun getItemCount() = matchPersons.size

}