package com.example.alf.ui.match.referees

import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.MainActivity
import com.example.alf.R
import com.example.alf.data.model.Referee
import com.example.alf.databinding.FragmentMatchRefereesBinding
import com.example.alf.ui.match.event.EventFragmentArgs
import com.google.android.material.snackbar.Snackbar


class MatchRefereesFragment : Fragment(), MatchRefereesAdapter.MatchRefereeListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
    }

    private lateinit var binding: FragmentMatchRefereesBinding

    private val args: EventFragmentArgs by navArgs()

    private lateinit var viewAdapter: MatchRefereesAdapter

    private val matchRefereesViewModel: MatchRefereesViewModel by viewModels {
        MatchRefereesViewModelFactory(
                requireActivity().application,
                requireArguments().getInt(ARG_MATCH_ID)
        )
    }

    var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchRefereesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.matchRefereesViewModel = matchRefereesViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initFab()

        matchRefereesViewModel.matchRefereesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                viewAdapter = MatchRefereesAdapter(this)
                viewAdapter.setReferees(it)
                binding.refereesRecyclerView.apply {
                    adapter = viewAdapter
                }
            } else {
                showSnackBar(binding.root, "Get match referees failed")
            }
        })
    }

    private fun initFab() {
        binding.fab.setOnClickListener { openRefereeSelection() }
        binding.refereesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun openRefereeSelection() {
        val action = MatchRefereesFragmentDirections.actionMatchRefereesFragmentToRefereeSelectionFragment(
            requireArguments().getInt(ARG_MATCH_ID)
        )
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(referee: Referee, position: Int) {
        deleteReferee(referee, position)
    }

    override fun onItemClick(referee: Referee) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View, referee: Referee, position: Int): Boolean {
        // Called when the user long-clicks on referee view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = (activity as MainActivity).startSupportActionMode(
                        RefereeActionModeCallback(this, referee, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    class RefereeActionModeCallback(
            private val matchRefereesFragment: MatchRefereesFragment,
            private val referee: Referee,
            private val position: Int
            ) : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_match_referees, menu)
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
                    matchRefereesFragment.onItemDeleted(referee, position)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            matchRefereesFragment.actionMode = null
        }
    }

    private fun deleteReferee(referee: Referee, position: Int) {
        showSnackBar(binding.root, "Delete $referee $position")
    }
}