package com.example.alf.ui.match.eventTypes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alf.data.model.event.EventType
import com.example.alf.databinding.FragmentEventTypeSelectionBinding
import com.example.alf.ui.match.players.selection.Mode
import com.google.android.material.snackbar.Snackbar

class EventTypeSelectionFragment : Fragment(), EventTypesAdapter.EventTypesListener {

    private lateinit var binding: FragmentEventTypeSelectionBinding

    private val args: EventTypeSelectionFragmentArgs by navArgs()

    private val eventTypeSelectionViewModel: EventTypeSelectionViewModel by viewModels  {
        EventTypeSelectionViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: EventTypesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventTypeSelectionBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.eventTypeSelectionViewModel = eventTypeSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeEventTypesViewModel()
    }

    private fun setupViews() {
        binding.eventTypesRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.retryButton.setOnClickListener { getEventTypes() }
    }

    private fun observeEventTypesViewModel() {
        eventTypeSelectionViewModel.eventTypesLiveData.observe(viewLifecycleOwner, {
            onGetEventTypesResult(it)
        })
    }

    private fun getEventTypes() {
        eventTypeSelectionViewModel.getEventTypes()
    }

    private fun onGetEventTypesResult(eventTypes: List<EventType>?) {
        eventTypes?.let {
            viewAdapter = EventTypesAdapter(it, this)
            binding.eventTypesRecyclerView.adapter = viewAdapter
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(eventType: EventType) {
        val action = EventTypeSelectionFragmentDirections.actionEventTypeSelectionFragmentToPlayerSelectionFragment(
                matchId = args.matchId,
                hostTeamId = args.hostTeamId, guestTeamId = args.guestTeamId,
                minute = args.minute,
                teamId = args.teamId, team = args.team,
                fieldPosition = null,
                eventType = eventType,
                playerOut = null,
                mode = Mode.EVENT_PLAYER
        )
        findNavController().navigate(action)
    }

}