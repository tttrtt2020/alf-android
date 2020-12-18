package com.example.alf.ui.match.team

import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.MainActivity
import com.example.alf.R
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.databinding.FragmentTeamBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.squad.MatchPlayersAdapter
import com.google.android.material.snackbar.Snackbar


class TeamFragment : Fragment(), MatchPlayersAdapter.SquadListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
        const val ARG_TEAM_SIDE = "teamSide"
        const val ARG_TEAM_SIDE_HOST = "host"
        const val ARG_TEAM_SIDE_GUEST = "guest"
        const val ARG_TEAM_ID = "teamId"
        const val ARG_TEAM = "team"
        const val ARG_FORMAT = "format"
    }

    private lateinit var binding: FragmentTeamBinding

    private val args: TeamFragmentArgs by navArgs()

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val teamViewModel: TeamViewModel by viewModels {
        TeamViewModelFactory(
                requireActivity().application,
                args.matchId,
                args.teamId
        )
    }

    private lateinit var viewAdapter: MatchPlayersAdapter

    private var formation: Formation? = null

    var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.teamViewModel = teamViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle()
        observeMatchTeamViewModel()
        setupFab()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_formations -> {
                openFormations()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setTitle() {
        (requireActivity() as MainActivity).supportActionBar?.title = args.team.name
    }

    private fun observeMatchTeamViewModel() {
        teamViewModel.matchTeamLiveData.observe(viewLifecycleOwner, {
            onGetTeamResult(it)
        })

        teamViewModel.formationLiveData.observe(viewLifecycleOwner, {
            formation = it
        })

        teamViewModel.deleteMatchPlayerLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onDeleteMatchPlayerResult(it)
                teamViewModel.deleteMatchPlayerLiveData.value = null
            }
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
        binding.matchPlayersRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun openFormations() {
        val action = TeamFragmentDirections.actionTeamFragmentToFormationsFragment()
        findNavController().navigate(action)
    }

    private fun onDeleteMatchPlayerResult(success: Boolean) {
        if (success) {
            teamViewModel.getSquadByMatchIdAndTeamId(args.matchId, args.teamId)
            //viewAdapter.deleteMatchPlayer(matchPlayer) todo: should do this but requires match player or position
            showSnackBar(binding.root, "Delete player success")
        } else showSnackBar(binding.root, "Delete player failed")
    }

    private fun onFabClicked() {
        if (formation == null) {
            openPlayerSelectionFragment()
        } else {
            openFieldPositionSelectionFragment()
        }
    }

    private fun openPlayerSelectionFragment() {
        val action = TeamFragmentDirections.actionTeamFragmentToPlayerSelectionFragment(
                args.matchId, args.teamId, args.team, args.format
        )
        findNavController().navigate(action)
    }

    private fun openFieldPositionSelectionFragment() {
        val action = TeamFragmentDirections.actionTeamFragmentToFieldPositionSelectionFragment(
                args.matchId, args.teamId, args.team, args.format
        )
        findNavController().navigate(action)
    }

    private fun onGetTeamResult(matchTeam: MatchTeam) {
        viewAdapter = MatchPlayersAdapter(this, args.format)
        viewAdapter.setMatchPlayers(matchTeam.matchPlayers)
        binding.matchPlayersRecyclerView.apply {
            adapter = viewAdapter
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPlayer: MatchPlayer, position: Int) {
        deletePlayer(matchPlayer.player, position)
    }

    override fun onItemClick(matchPlayer: MatchPlayer) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View, matchPlayer: MatchPlayer, position: Int): Boolean {
        // Called when the user long-clicks on match person view
        return when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = (activity as MainActivity).startSupportActionMode(
                        MatchPlayerActionModeCallback(this, matchPlayer, position)
                )
                view.isSelected = true
                true
            }
            else -> false
        }
    }

    private fun deletePlayer(player: Player, position: Int) {
        showSnackBar(binding.root, "Delete $player $position")

        teamViewModel.deleteMatchPlayer(args.matchId, player)
    }

    class MatchPlayerActionModeCallback(
            private val teamFragment: TeamFragment,
            private val matchPlayer: MatchPlayer,
            private val position: Int
    ) : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.context_team_players, menu)
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
                    teamFragment.onItemDeleted(matchPlayer, position)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            teamFragment.actionMode = null
        }
    }

}