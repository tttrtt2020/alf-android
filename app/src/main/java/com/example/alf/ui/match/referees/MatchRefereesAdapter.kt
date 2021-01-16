package com.example.alf.ui.match.referees;

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
import com.example.alf.data.model.Referee
import com.example.alf.databinding.ItemRefereeBinding

class MatchRefereesAdapter(
        private var referees: ArrayList<Referee>,
        private var listener: MatchRefereeListener
) : RecyclerView.Adapter<MatchRefereesAdapter.ViewHolder>() {

    companion object {

        private fun buildPersonPhotoUrl(referee: Referee): String {
            return AlfApplication.getProperty("url.image.referee") +
                    referee.id +
                    AlfApplication.getProperty("extension.image.referee")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPersonPhoto(imageView: ImageView, referee: Referee) {
            val url = buildPersonPhotoUrl(referee)
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

    interface MatchRefereeListener {
        fun onItemDeleted(referee: Referee, position: Int)

        fun onItemClick(referee: Referee)

        fun onItemLongClick(view: View, referee: Referee, position: Int): Boolean
    }

    fun setReferees(list: List<Referee>) {
        referees = if (list is ArrayList) list else ArrayList(list)
        notifyDataSetChanged()
    }

    fun removeReferee(position: Int, resultCallback: () -> Unit) {
        referees.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, referees.size)
        if (position == 0) resultCallback()
    }

    inner class ViewHolder(private val binding: ItemRefereeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(referee: Referee) {
            binding.referee = referee
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRefereeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val referee = referees[position]
        holder.bind(referee)
        holder.itemView.setOnClickListener { listener.onItemClick(referee) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, referee, position) }
    }

    override fun getItemCount() = referees.size

}