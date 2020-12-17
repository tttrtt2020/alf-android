package com.example.alf.ui.match.team

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.MainActivity
import com.example.alf.R
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.databinding.FragmentTeamBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.squad.MatchPersonsAdapter
import com.google.android.material.snackbar.Snackbar


class TeamFragment : Fragment(), MatchPersonsAdapter.SquadListener {

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

    private lateinit var viewAdapter: MatchPersonsAdapter

    private var formation: Formation? = null

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
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { onFabClicked() }
        binding.matchPersonsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun onFabClicked() {
        //if (teamViewModel.formationLiveData.value == null) {
        if (formation == null) {
            val action = TeamFragmentDirections.actionTeamFragmentToPlayerSelectionFragment(
                    args.matchId, args.teamId, args.team, args.format
            )
            findNavController().navigate(action)
        } else {
            val action = TeamFragmentDirections.actionTeamFragmentToFieldPositionSelectionFragment(
                    args.matchId, args.teamId, args.team, args.format
            )
            findNavController().navigate(action)
        }
    }

    private fun onGetTeamResult(matchTeam: MatchTeam) {
        viewAdapter = MatchPersonsAdapter(this, args.format)
        viewAdapter.setMatchPersons(matchTeam.matchPlayers)
        binding.matchPersonsRecyclerView.apply {
            adapter = viewAdapter
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPerson: MatchPerson, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchPerson: MatchPerson) {
        TODO("Not yet implemented")
    }

}