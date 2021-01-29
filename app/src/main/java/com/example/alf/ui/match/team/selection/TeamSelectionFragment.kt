package com.example.alf.ui.match.team.selection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alf.R
import com.example.alf.data.model.Team
import com.example.alf.databinding.FragmentTeamSelectionBinding
import com.google.android.material.snackbar.Snackbar


class TeamSelectionFragment : Fragment(), TeamsAdapter.TeamsListener {

    private lateinit var binding: FragmentTeamSelectionBinding

    private val args: TeamSelectionFragmentArgs by navArgs()

    private val teamSelectionViewModel: TeamSelectionViewModel by viewModels {
        TeamSelectionViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: TeamsAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamSelectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.teamSelectionViewModel = teamSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeTeamSelectionViewModel()
    }

    private fun setupViews() {
        binding.teamsRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.retryButton.setOnClickListener { getTeams() }
    }

    private fun observeTeamSelectionViewModel() {
        teamSelectionViewModel.teamsLiveData.observe(viewLifecycleOwner, {
            onGetTeamsResult(it)
        })
    }

    private fun getTeams() {
        teamSelectionViewModel.getTeams()
    }

    private fun openPlayerSelection(team: Team) {
        val action = when (args.mode) {
            Mode.EVENT_TEAM -> TeamSelectionFragmentDirections.actionTeamSelectionFragmentToEventTypeSelectionFragment(
                    matchId = args.matchId,
                    hostTeamId = args.hostTeamId, guestTeamId = args.guestTeamId,
                    minute = args.minute,
                    teamId = team.id, team = team
            )
            Mode.SUBSTITUTION_TEAM -> TeamSelectionFragmentDirections.actionTeamSelectionFragmentToPlayerSelectionFragment(
                    matchId = args.matchId,
                    hostTeamId = args.hostTeamId, guestTeamId = args.guestTeamId,
                    minute = args.minute,
                    teamId = team.id, team = team,
                    fieldPosition = null,
                    eventType = null,
                    playerOut = null,
                    mode = com.example.alf.ui.match.players.selection.Mode.SUBSTITUTION_OUT_PLAYER
            )
        }
        findNavController().navigate(action)
    }

    private fun onGetTeamsResult(teams: List<Team>?) {
        teams?.let {
            viewAdapter = TeamsAdapter(it, this)
            binding.teamsRecyclerView.adapter = viewAdapter
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(team: Team) {
        openPlayerSelection(team)
    }

}