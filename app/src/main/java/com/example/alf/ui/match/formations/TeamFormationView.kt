package com.example.alf.ui.match.formations

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.example.alf.R

class TeamFormationView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    interface OnChangeFormationClickListener {
        fun setOnChangeFormationClickListener()
    }

    private lateinit var listener: OnChangeFormationClickListener

    //private lateinit var matchPersons: List<MatchPerson>

    init {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.team_formation_view, this, true)

        val changeFormationView = view.findViewById<Button>(R.id.change_formation)
        //val formationView = view.findViewById<FormationView>(R.id.formation)

        changeFormationView.visibility = VISIBLE
        //formationView.visibility = GONE

        changeFormationView.setOnClickListener { listener.setOnChangeFormationClickListener() }
    }

    /*fun setMatchPersons(squad: List<MatchPerson>) {
        matchPersons = squad
        val view = LayoutInflater.from(context)
                .inflate(R.layout.team_formation_view, this, true)
        val formationView = view.findViewById<FormationView>(R.id.formation)
        formationView.fieldPositions = matchPersons.mapNotNull { mpm -> mpm.fieldPosition }
        val changeFormationView = view.findViewById<Button>(R.id.change_formation)
        if (formationView.fieldPositions.isEmpty()) {
            changeFormationView.visibility = GONE
            formationView.visibility = GONE
        } else {
            changeFormationView.visibility = VISIBLE
            formationView.visibility = VISIBLE
            changeFormationView.setOnClickListener { listener.setOnChangeFormationClickListener() }
        }
        invalidate()
    }*/

    fun setOnChangeFormationClickListener(listener: OnChangeFormationClickListener) {
        this.listener = listener
    }

}