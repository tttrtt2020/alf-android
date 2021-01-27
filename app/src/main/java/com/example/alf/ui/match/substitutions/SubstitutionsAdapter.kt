package com.example.alf.ui.match.substitutions;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.Substitution
import com.example.alf.databinding.ItemSubstitutionBinding

class SubstitutionsAdapter(
        substitutions: List<Substitution>,
        private var hostTeamId: Int,
        private var guestTeamId: Int,
        private var listener: SubstitutionsListener
) : RecyclerView.Adapter<SubstitutionsAdapter.ViewHolder>() {

    private var substitutions = if (substitutions is ArrayList) substitutions else ArrayList(substitutions)

    fun setSubstitutions(substitutions: List<Substitution>) {
        this.substitutions = if (substitutions is ArrayList) substitutions else ArrayList(substitutions)
        notifyDataSetChanged()
    }

    fun removeSubstitution(position: Int, resultCallback: () -> Unit) {
        substitutions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, substitutions.size)
        if (position == 0) resultCallback()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSubstitutionBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val substitution = substitutions[position]
        holder.bind(substitution)
        holder.itemView.setOnClickListener { listener.onItemClick(substitution) }
        holder.itemView.setOnLongClickListener { listener.onItemLongClick(it, substitution, position) }
    }

    override fun getItemCount() = substitutions.size

    inner class ViewHolder(private val binding: ItemSubstitutionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(substitution: Substitution) {
            binding.substitution = substitution
            binding.hostTeamId = hostTeamId
            binding.guestTeamId = guestTeamId
            binding.executePendingBindings()
        }
    }

    interface SubstitutionsListener {
        fun onItemClick(substitution: Substitution)

        fun onItemLongClick(view: View, substitution: Substitution, position: Int): Boolean

        fun onItemDeleted(substitution: Substitution, position: Int)
    }

}