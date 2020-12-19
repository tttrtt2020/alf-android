package com.example.alf.ui.match.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.FragmentEventsBinding
import com.google.android.material.snackbar.Snackbar


class EventsFragment : Fragment(), EventsAdapter.EventsListener {

    /*companion object {
        const val ARG_MATCH_ID = "matchId"
        const val ARG_HOST_TEAM_ID = "hostTeamId"
        const val ARG_GUEST_TEAM_ID = "guestTeamId"
    }*/

    private lateinit var binding: FragmentEventsBinding

    private val args: EventsFragmentArgs by navArgs()

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    //private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val eventsViewModel: EventsViewModel by viewModels {
        EventsViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.eventsViewModel = eventsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = EventsAdapter(
                args.hostTeamId,
                args.guestTeamId,
                this
        )
        binding.eventsRecyclerView.apply {
            adapter = viewAdapter
        }

        eventsViewModel.eventsLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                viewAdapter.setEvents(eventsViewModel.eventsLiveData.value!!)
            } else {
                showSnackBar(binding.root, "Get events failed")
            }
        })

        setupFab()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
        binding.eventsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.fab.hide()
                } else {
                    binding.fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun onFabClicked() {
        val action = EventsFragmentDirections.actionEventsFragmentToLiveEventTypesFragment(args.matchId)
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(event: Event, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(event: Event) {
        val action = EventsFragmentDirections.actionEventsFragmentToEventFragment(
                args.matchId,
                event.id,
                event
        )
        findNavController().navigate(action)
    }
}