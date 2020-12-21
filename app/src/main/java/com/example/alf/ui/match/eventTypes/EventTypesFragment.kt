package com.example.alf.ui.match.eventTypes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.event.EventType
import com.example.alf.databinding.FragmentEventTypesBinding
import com.google.android.material.snackbar.Snackbar

class EventTypesFragment : Fragment(), EventTypesAdapter.EventTypesListener {

    private lateinit var binding: FragmentEventTypesBinding

    private val args: EventTypesFragmentArgs by navArgs()

    private val eventTypesViewModel: EventTypesViewModel by viewModels  {
        EventTypesViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: EventTypesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventTypesBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.eventTypesViewModel = eventTypesViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEventTypesViewModel()
    }

    private fun observeEventTypesViewModel() {
        eventTypesViewModel.eventTypesLiveData.observe(viewLifecycleOwner, {
            onGetEventTypesResult(it)
        })
    }

    private fun onGetEventTypesResult(eventTypes: List<EventType>?) {
        if (eventTypes != null) {
            viewAdapter = EventTypesAdapter(eventTypes, this)
            binding.eventTypesRecyclerView.adapter = viewAdapter
        } else showSnackBar(binding.root, "Get event types failed")
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