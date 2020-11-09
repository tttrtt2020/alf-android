package com.example.alf.ui.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alf.ui.match.events.EventsFragment
import com.example.alf.ui.match.squads.SquadsFragment

class MatchInfoAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val ARG_OBJECT = "object"
    }

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        /*val fragment: Fragment = if (position == 0) {
            SquadsFragment()
        } else {
            EventsFragment()
        }
        fragment.*/

        val fragment = if (position == 0) SquadsFragment() else if (position == 1) EventsFragment() else SquadsFragment()

        //fragment.arguments.putString("team", if (position == 0) "host" else "guest")
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
            putString("team", if (position == 0) "host" else if (position == 2) "guest" else "")
        }
        return fragment
    }
}