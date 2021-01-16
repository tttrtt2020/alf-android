package com.example.alf.ui.match.referees

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
import com.example.alf.data.model.Referee
import com.example.alf.databinding.FragmentMatchRefereesBinding
import com.example.alf.ui.common.ActionModeCallback
import com.google.android.material.snackbar.Snackbar


class MatchRefereesFragment : Fragment(), MatchRefereesAdapter.MatchRefereeListener {

    private lateinit var binding: FragmentMatchRefereesBinding

    private val args: MatchRefereesFragmentArgs by navArgs()

    private lateinit var viewAdapter: MatchRefereesAdapter

    private val matchRefereesViewModel: MatchRefereesViewModel by viewModels {
        MatchRefereesViewModelFactory(
                args.matchId
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
        setupViews()
        observeRefereesViewModel()
        setupFab()
    }

    private fun observeRefereesViewModel() {
        matchRefereesViewModel.refereesLiveData.observe(viewLifecycleOwner, {
            onGetRefereesResult(it)
        })

        matchRefereesViewModel.deleteRefereeActionLiveData.observe(viewLifecycleOwner) { viewEvent ->
            viewEvent.getContentIfNotHandledOrReturnNull()?.let {
                onDeleteMatchRefereeResult(it)
            }
        }
    }

    private fun onGetRefereesResult(referees: List<Referee>?) {
        referees?.let {
            viewAdapter = MatchRefereesAdapter(it, this)
            binding.refereesRecyclerView.adapter = viewAdapter
        }
    }

    private fun setupViews() {
        binding.refereesRecyclerView.apply {
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
        binding.retryButton.setOnClickListener { getMatchReferees() }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
    }

    private fun getMatchReferees() {
        matchRefereesViewModel.getReferees()
    }

    private fun onDeleteMatchRefereeResult(position: Int) {
        if (position >= 0) {
            viewAdapter.removeReferee(position) {
                // reset to enable empty state
                matchRefereesViewModel.reset()
            }
            showSnackBar(binding.root, "Delete match referee success")
        } else showSnackBar(binding.root, "Delete match referee failed")
    }

    private fun onFabClicked() {
        openRefereeSelection()
    }

    private fun openRefereeSelection() {
        val action = MatchRefereesFragmentDirections.actionMatchRefereesFragmentToRefereeSelectionFragment(
            args.matchId
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
        showSnackBar(binding.root, "Not used")
    }

    override fun onItemLongClick(view: View, referee: Referee, position: Int): Boolean {
        // Called when the user long-clicks on referee view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback
                actionMode = (activity as MainActivity).startSupportActionMode(
                        RefereeActionModeCallback(this, view, referee, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    private fun deleteReferee(referee: Referee, position: Int) {
        matchRefereesViewModel.deleteReferee(referee, position)
    }

    class RefereeActionModeCallback(
            private val matchRefereesFragment: MatchRefereesFragment,
            view: View,
            private val referee: Referee,
            private val position: Int
    ) : ActionModeCallback(matchRefereesFragment.requireContext(), view) {

        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_match_referees, menu)
            return true
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
            super.onDestroyActionMode(mode)
            matchRefereesFragment.actionMode = null
        }
    }
}