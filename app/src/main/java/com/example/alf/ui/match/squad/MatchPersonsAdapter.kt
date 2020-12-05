package com.example.alf.ui.match.squad;

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.data.model.match.Player
import com.example.alf.databinding.ItemMatchPersonBinding

class MatchPersonsAdapter(var listener: SquadListener) :
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
    }

    private var matchPersons: List<MatchPerson> = ArrayList()

    interface SquadListener {
        fun onItemDeleted(matchPerson: MatchPerson, position: Int)

        fun onItemClick(matchPerson: MatchPerson)
    }

    fun setMatchPersons(list: List<MatchPerson>) {
        matchPersons = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemMatchPersonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(matchPerson: MatchPerson) {
            binding.matchPerson = matchPerson
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
    }

    override fun getItemCount() = matchPersons.size

}