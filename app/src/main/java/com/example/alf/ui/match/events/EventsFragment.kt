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
    }

    private fun observeEventsViewModel() {
        eventsViewModel.eventsLiveData.observe(viewLifecycleOwner, {
            onGetEventsResult(it)
        })

        eventsViewModel.deleteEventLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onDeleteEventResult(it)
                eventsViewModel.deleteEventLiveData.value = null
            }
        }
    }

    private fun onGetEventsResult(events: List<Event>?) {
        if (events != null) {
            viewAdapter = EventsAdapter(events, args.hostTeamId, args.guestTeamId, this)
            binding.eventsRecyclerView.adapter = viewAdapter
        } else showSnackBar(binding.root, "Get events failed")
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
    }

    private fun onDeleteEventResult(success: Boolean) {
        if (success) {
            eventsViewModel.getEvents()
            //viewAdapter.deleteMatchPlayer(matchPlayer) todo: should do this but requires match player or position
            showSnackBar(binding.root, "Delete event success")
        } else showSnackBar(binding.root, "Delete event failed")
    }

    private fun onFabClicked() {
        val action = EventsFragmentDirections.actionEventsFragmentToEventTypesFragment(args.matchId)
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(event: Event, position: Int) {
        deleteEvent(event, position)
    }

    override fun onItemClick(event: Event) {
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
        eventsViewModel.deleteEvent(event)
    }

    override fun onItemLongClick(view: View, event: Event, position: Int): Boolean {
        // Called when the user long-clicks on match person view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = (activity as MainActivity).startSupportActionMode(
                        EventActionModeCallback(this, event, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    class EventActionModeCallback(
            private val eventsFragment: EventsFragment,
            private val event: Event,
            private val position: Int
    ) : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_events, menu)
            return true
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
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
            eventsFragment.actionMode = null
        }
    }
}