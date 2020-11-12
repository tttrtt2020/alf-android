package com.example.alf.ui.match.formations;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.match.FormationModel
import kotlin.collections.ArrayList

class FormationsAdapter(var listener: FormationsListener) :
        RecyclerView.Adapter<FormationsAdapter.ViewHolder>() {

    private var formations: ArrayList<FormationModel>? = null

    interface FormationsListener {
        fun onItemClick(formationModel: FormationModel, position: Int)
    }

    fun setFormations(list: ArrayList<FormationModel>) {
        formations = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.name)
        var formationView: FormationView = itemView.findViewById(R.id.formation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_row_formation, parent, false) as View
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formation = formations?.get(position)

        holder.nameTextView.text = formation!!.name

        holder.formationView.fieldPositions = formation.fieldPositions

        holder.itemView.setOnClickListener {
            formation.let { it1 -> listener.onItemClick(it1, position) }
        }
    }

    override fun getItemCount() = formations?.size ?: 0

}