package com.example.alf.ui.match.eventTypes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.event.EventType
import com.example.alf.databinding.FragmentEventTypesBinding
import com.google.android.material.snackbar.Snackbar

class EventTypesFragment : Fragment(), EventTypesAdapter.EventTypesListener {

    private lateinit var binding: FragmentEventTypesBinding

    private lateinit var viewAdapter: EventTypesAdapter

    private val args: EventTypesFragmentArgs by navArgs()

    private val eventTypesViewModel: EventTypesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventTypesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = EventTypesAdapter(this)
        binding.eventTypesRecyclerView.apply {
            adapter = viewAdapter
        }

        eventTypesViewModel.eventTypesLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.eventTypesRecyclerView.visibility = View.VISIBLE
                viewAdapter.setEventTypes(eventTypesViewModel.eventTypesLiveData.value!!)

                binding.progressBar.visibility = View.GONE
            } else {
                showSnackBar(binding.root, "Something went wrong")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(eventType: EventType) {
        /*val action = eventType.id.let {
            EventTypesFragmentDirections.actionEventTypesFragmentToEventFragment(
                    matchId = args.matchId,
                    eventTypeId = it,
                    eventType = eventType
            )
        }
        findNavController().navigate(action)*/
    }
}