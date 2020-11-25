package com.example.alf.ui.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alf.ui.match.events.EventsFragment
import com.example.alf.ui.match.squads.SquadsFragment

class MatchInfoAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val ARG_TEAM = "team"
        const val ARG_TEAM_HOST = "host"
        const val ARG_TEAM_GUEST = "guest"
    }

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return SquadsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TEAM, ARG_TEAM_HOST)
                    }
                }
            }
            2 -> {
                return SquadsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TEAM, ARG_TEAM_GUEST)
                    }
                }
            }
            1 -> {
                return EventsFragment()
            }
            else -> throw IllegalArgumentException("position argument can only be one of (0, 1, 2)")
        }

    }
}