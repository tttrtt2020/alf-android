package com.example.alf.ui.match.team.selection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupViews() {
        binding.teamsRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeTeamSelectionViewModel() {
        teamSelectionViewModel.teamsLiveData.observe(viewLifecycleOwner, {
            onGetTeamsResult(it)
        })
    }

    private fun openPlayerSelection() {
        /*val action = TeamSelectionFragmentDirections.actionTeamFragmentToPlayerSelectionFragment(
                args.matchId, args.teamId
        )
        findNavController().navigate(action)*/
    }

    private fun onGetTeamsResult(teams: List<Team>?) {
        if (teams != null) {
            viewAdapter = TeamsAdapter(teams, this)
            binding.teamsRecyclerView.adapter = viewAdapter
        } else {
            showSnackBar(binding.root, "Get teams failed")
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(team: Team) {
        openPlayerSelection()
    }

}