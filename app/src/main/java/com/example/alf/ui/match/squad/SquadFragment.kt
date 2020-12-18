package com.example.alf.ui.match.squad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.alf.R
import com.example.alf.data.model.Match
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.databinding.FragmentSquadBinding
import com.example.alf.ui.match.MatchFragmentDirections
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.formations.TeamFormationView
import com.google.android.material.snackbar.Snackbar


class SquadFragment : Fragment(), MatchPlayersAdapter.SquadListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
        const val ARG_TEAM_SIDE = "teamSide"
        const val ARG_TEAM_SIDE_HOST = "host"
        const val ARG_TEAM_SIDE_GUEST = "guest"
        const val ARG_TEAM_ID = "teamId"
    }

    private lateinit var binding: FragmentSquadBinding

    private lateinit var viewAdapter: MatchPlayersAdapter

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val squadViewModel: SquadViewModel by viewModels {
        SquadViewModelFactory(
                requireActivity().application,
                requireArguments().getInt(ARG_MATCH_ID),
                requireArguments().getInt(ARG_TEAM_ID)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSquadBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.squadViewModel = squadViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.teamFormation.setOnChangeFormationClickListener(this)

        matchViewModel.matchLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                loadSquad()
            }
        })
    }

    private fun loadSquad() {
        squadViewModel.squadLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                viewAdapter = MatchPlayersAdapter(this, match.format)
                viewAdapter.setMatchPlayers(it)
                binding.matchPlayersRecyclerView.apply {
                    adapter = viewAdapter
                }
            } else {
                showSnackBar(binding.root, "Get squad failed")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPlayer: MatchPlayer, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchPlayer: MatchPlayer) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(view: View, matchPlayer: MatchPlayer, position: Int): Boolean {
        TODO("Not yet implemented")
    }
}