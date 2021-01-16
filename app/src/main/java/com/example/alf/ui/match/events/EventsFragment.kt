package com.example.alf.ui.match.events

import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.MainActivity
import com.example.alf.R
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.FragmentEventsBinding
import com.example.alf.ui.common.ActionModeCallback
import com.google.android.material.snackbar.Snackbar


class EventsFragment : Fragment(), EventsAdapter.EventsListener {

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

    var actionMode: ActionMode? = null

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
        setupViews()
        observeEventsViewModel()
        setupFab()
    }

    private fun setupViews() {
        binding.eventsRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding.retryButton.setOnClickListener { getEvents() }
    }

    private fun observeEventsViewModel() {
        eventsViewModel.eventsLiveData.observe(viewLifecycleOwner, {
            onGetEventsResult(it)
        })

        eventsViewModel.deleteEventActionLiveData.observe(viewLifecycleOwner) { viewEvent ->
            viewEvent.getContentIfNotHandledOrReturnNull()?.let {
                onDeleteEventResult(it)
            }
        }
    }

    private fun getEvents() {
        eventsViewModel.getEvents()
    }

    private fun onGetEventsResult(events: List<Event>?) {
        events?.let {
            viewAdapter = EventsAdapter(it, args.hostTeamId, args.guestTeamId, this)
            binding.eventsRecyclerView.adapter = viewAdapter
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
    }

    private fun onDeleteEventResult(position: Int) {
        if (position >= 0) {
            viewAdapter.removeEvent(position) {
                // reset to enable empty state
                eventsViewModel.reset()
            }
            showSnackBar(binding.root, "Delete event success")
        } else showSnackBar(binding.root, "Delete event failed")
    }

    private fun onFabClicked() {
        val action = EventsFragmentDirections.actionEventsFragmentToEventTypesFragment(
                args.matchId, args.hostTeamId, args.guestTeamId
        )
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(event: Event) {
        //openEvent(event)
    }

    override fun onItemDeleted(event: Event, position: Int) {
        deleteEvent(event, position)
    }

    override fun onItemLongClick(view: View, event: Event, position: Int): Boolean {
        return startActionMode(view, event, position)
    }

    private fun openEvent(event: Event) {
        val action = EventsFragmentDirections.actionEventsFragmentToEventFragment(
                args.matchId,
                event.id,
                event,
                event.eventType.id,
                event.eventType
        )
        findNavController().navigate(action)
    }

    private fun deleteEvent(event: Event, position: Int) {
        eventsViewModel.deleteEvent(event, position)
    }

    private fun startActionMode(view: View, event: Event, position: Int): Boolean {
        // Called when the user long-clicks on match person view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback
                actionMode = (activity as MainActivity).startSupportActionMode(
                        EventActionModeCallback(this, view, event, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    class EventActionModeCallback(
            private val eventsFragment: EventsFragment,
            view: View,
            private val event: Event,
            private val position: Int
    ) : ActionModeCallback(eventsFragment.requireContext(), view) {

        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_events, menu)
            return true
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    eventsFragment.onItemDeleted(event, position)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            super.onDestroyActionMode(mode)
            eventsFragment.actionMode = null
        }
    }
}