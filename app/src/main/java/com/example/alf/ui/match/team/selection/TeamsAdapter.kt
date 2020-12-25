package com.example.alf.ui.match.team.selection;

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.data.model.Team
import com.example.alf.databinding.ItemTeamBinding

class TeamsAdapter(
        private var teams: List<Team>,
        private var listener: TeamsListener
) : RecyclerView.Adapter<TeamsAdapter.ViewHolder>() {

    companion object {

        private fun buildTeamLogoUrl(team: Team): String {
            return AlfApplication.getProperty("url.logo.club") +
                    team.club.id +
                    AlfApplication.getProperty("extension.logo.club")
        }

        @JvmStatic
        @BindingAdapter("app:imageSrc")
        fun loadTeamLogo(imageView: ImageView, team: Team) {
            val url = buildTeamLogoUrl(team)
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

    interface TeamsListener {
        fun onItemClick(team: Team)
    }

    fun setTeams(list: List<Team>) {
        teams = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            binding.team = team
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTeamBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
        holder.itemView.setOnClickListener { listener.onItemClick(team) }
    }

    override fun getItemCount() = teams.size

}