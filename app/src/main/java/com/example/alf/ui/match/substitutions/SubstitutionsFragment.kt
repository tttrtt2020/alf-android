package com.example.alf.ui.match.substitutions

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
import com.example.alf.data.model.Substitution
import com.example.alf.databinding.FragmentSubstitutionsBinding
import com.example.alf.ui.common.ActionModeCallback
import com.google.android.material.snackbar.Snackbar


class SubstitutionsFragment : Fragment(), SubstitutionsAdapter.SubstitutionsListener {

    private lateinit var binding: FragmentSubstitutionsBinding

    private val args: SubstitutionsFragmentArgs by navArgs()

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    //private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val substitutionsViewModel: SubstitutionsViewModel by viewModels {
        SubstitutionsViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: SubstitutionsAdapter

    var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubstitutionsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.substitutionsViewModel = substitutionsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeSubstitutionsViewModel()
        setupFab()
    }

    private fun setupViews() {
        binding.substitutionsRecyclerView.apply {
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
        binding.retryButton.setOnClickListener { getSubstitutions() }
    }

    private fun observeSubstitutionsViewModel() {
        substitutionsViewModel.substitutionsLiveData.observe(viewLifecycleOwner, {
            onGetSubstitutionsResult(it)
        })

        substitutionsViewModel.deleteSubstitutionActionLiveData.observe(viewLifecycleOwner) { viewSubstitution ->
            viewSubstitution.getContentIfNotHandledOrReturnNull()?.let {
                onDeleteSubstitutionResult(it)
            }
        }
    }

    private fun getSubstitutions() {
        substitutionsViewModel.getSubstitutions()
    }

    private fun onGetSubstitutionsResult(substitutions: List<Substitution>?) {
        substitutions?.let {
            viewAdapter = SubstitutionsAdapter(it, args.hostTeamId, args.guestTeamId, this)
            binding.substitutionsRecyclerView.adapter = viewAdapter
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
    }

    private fun onDeleteSubstitutionResult(position: Int) {
        if (position >= 0) {
            viewAdapter.removeSubstitution(position) {
                // reset to enable empty state
                substitutionsViewModel.reset()
            }
            showSnackBar(binding.root, "Delete substitution success")
        } else showSnackBar(binding.root, "Delete substitution failed")
    }

    private fun onFabClicked() {
        /*val action = SubstitutionsFragmentDirections.actionSubstitutionsFragmentToSubstitutionTypesFragment(
                args.matchId, args.hostTeamId, args.guestTeamId
        )
        findNavController().navigate(action)*/
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(substitution: Substitution) {
        //openSubstitution(substitution)
    }

    override fun onItemDeleted(substitution: Substitution, position: Int) {
        deleteSubstitution(substitution, position)
    }

    override fun onItemLongClick(view: View, substitution: Substitution, position: Int): Boolean {
        return startActionMode(view, substitution, position)
    }

    /*private fun openSubstitution(substitution: Substitution) {
        val action = SubstitutionsFragmentDirections.actionSubstitutionsFragmentToSubstitutionFragment(
                args.matchId,
                substitution.id,
                substitution,
                substitution.substitutionType.id,
                substitution.substitutionType
        )
        findNavController().navigate(action)
    }*/

    private fun deleteSubstitution(substitution: Substitution, position: Int) {
        substitutionsViewModel.deleteSubstitution(substitution, position)
    }

    private fun startActionMode(view: View, substitution: Substitution, position: Int): Boolean {
        // Called when the user long-clicks on match person view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback
                actionMode = (activity as MainActivity).startSupportActionMode(
                        SubstitutionActionModeCallback(this, view, substitution, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    class SubstitutionActionModeCallback(
            private val substitutionsFragment: SubstitutionsFragment,
            view: View,
            private val substitution: Substitution,
            private val position: Int
    ) : ActionModeCallback(substitutionsFragment.requireContext(), view) {

        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_substitutions, menu)
            return true
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    substitutionsFragment.onItemDeleted(substitution, position)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            super.onDestroyActionMode(mode)
            substitutionsFragment.actionMode = null
        }
    }
}