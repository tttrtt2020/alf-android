package com.example.alf.ui.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alf.ui.match.events.EventsFragment

class MatchInfoAdapter(
        fragment: Fragment,
        val matchId: Int,
        val hostTeamId: Int,
        val guestTeamId: Int
        ) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun createFragment(position: Int): Fragment {
        return Fragment()
        /*when (position) {
            0 -> {
                return SquadFragment().apply {
                    arguments = Bundle().apply {
                        putString(SquadFragment.ARG_TEAM_SIDE, SquadFragment.ARG_TEAM_SIDE_HOST)
                        putInt(SquadFragment.ARG_MATCH_ID, matchId)
                        putInt(SquadFragment.ARG_TEAM_ID, hostTeamId)
                    }
                }
            }
            2 -> {
                return SquadFragment().apply {
                    arguments = Bundle().apply {
                        putString(SquadFragment.ARG_TEAM_SIDE, SquadFragment.ARG_TEAM_SIDE_GUEST)
                        putInt(SquadFragment.ARG_MATCH_ID, matchId)
                        putInt(SquadFragment.ARG_TEAM_ID, guestTeamId)
                    }
                }
            }
            1 -> {
                return EventsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(EventsFragment.ARG_MATCH_ID, matchId)
                        putInt(EventsFragment.ARG_HOST_TEAM_ID, hostTeamId)
                        putInt(EventsFragment.ARG_GUEST_TEAM_ID, guestTeamId)
                    }
                }
            }
            else -> throw IllegalArgumentException("position argument can only be one of (0, 1, 2)")
        }*/

    }
}