package com.example.alf.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alf.R

class PersonsFragment : Fragment() {

    private lateinit var personsViewModel: PersonsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        personsViewModel =
                ViewModelProvider(this).get(PersonsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_persons, container, false)
        val textView: TextView = root.findViewById(R.id.text_persons)
        personsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}