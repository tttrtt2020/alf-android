package com.example.alf.ui.match.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.example.alf.R
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.FragmentEventsBinding
import com.example.alf.ui.match.MatchFragment
import com.example.alf.ui.match.MatchViewModel
import com.google.android.material.snackbar.Snackbar


class EventsFragment : Fragment(), EventsAdapter.EventsListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
    }

    private lateinit var binding: FragmentEventsBinding

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val eventsViewModel: EventsViewModel by viewModels {
        EventsViewModelFactory(
                requireActivity().application!!,
                requireArguments().getInt(ARG_MATCH_ID)
        )
    }

    private lateinit var viewAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = EventsAdapter(
                matchViewModel.matchLiveData.value!!.hostTeam.id,
                matchViewModel.matchLiveData.value!!.guestTeam.id,
                this
        )
        binding.eventsRecyclerView.apply {
            adapter = viewAdapter
        }

        eventsViewModel.eventsLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.eventsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setEvents(eventsViewModel.eventsLiveData.value!!)

                binding.progressBar.visibility = View.GONE
            } else {
                showSnackBar(binding.root, "Get events failed")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(event: Event, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(event: Event) {
        (parentFragment as MatchFragment).onEventClicked(event)
    }
}