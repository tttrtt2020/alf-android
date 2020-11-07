package com.example.alf.ui.match.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alf.R


class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val textView: TextView = view.findViewById(android.R.id.text1)
            textView.text = getInt(ARG_OBJECT).toString()
        }*/
    }
}