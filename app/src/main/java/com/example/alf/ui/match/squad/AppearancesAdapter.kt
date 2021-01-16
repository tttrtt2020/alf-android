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
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Appearance
import com.example.alf.databinding.ItemAppearanceBinding

class AppearancesAdapter(
        private var appearances: ArrayList<Appearance>,
        private var listener: SquadListener
) : RecyclerView.Adapter<AppearancesAdapter.ViewHolder>() {

    companion object {

        private fun buildPlayerPhotoUrl(player: Player): String {
            return AlfApplication.getProperty("url.image.player") +
                    player.id +
                    AlfApplication.getProperty("extension.image.player")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadPlayerPhoto(imageView: ImageView, player: Player) {
            val url = buildPlayerPhotoUrl(player)
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

    interface SquadListener {
        fun onItemDeleted(appearance: Appearance, position: Int)

        fun onItemClick(appearance: Appearance)

        fun onItemLongClick(view: View, appearance: Appearance, position: Int): Boolean
    }

    fun setAppearances(list: ArrayList<Appearance>) {
        appearances = list
        notifyDataSetChanged()
    }

    fun removeAppearance(position: Int, resultCallback: () -> Unit) {
        appearances.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, appearances.size)
        if (position == 0) resultCallback()
    }

    inner class ViewHolder(private val binding: ItemAppearanceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appearance: Appearance) {
            binding.appearance = appearance

            // todo: choose correct logic
            binding.inStart = appearance.fieldPosition != null
            //binding.inStart = appearance.timeIn == 0

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAppearanceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appearance = appearances[position]
        holder.bind(appearance)
        holder.itemView.setOnClickListener { listener.onItemClick(appearance) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, appearance, position) }
    }

    override fun getItemCount() = appearances.size

}