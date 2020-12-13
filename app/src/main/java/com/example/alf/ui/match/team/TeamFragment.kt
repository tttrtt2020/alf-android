package com.example.alf.ui.match.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.alf.MainActivity
import com.example.alf.R
import com.example.alf.data.model.Match
import com.example.alf.databinding.FragmentTeamBinding
import com.example.alf.ui.match.MatchViewModel
import com.google.android.material.snackbar.Snackbar


class TeamFragment : Fragment() {

    companion object {
        const val ARG_MATCH_ID = "matchId"
        const val ARG_TEAM_SIDE = "teamSide"
        const val ARG_TEAM_SIDE_HOST = "host"
        const val ARG_TEAM_SIDE_GUEST = "guest"
        const val ARG_TEAM_ID = "teamId"
        const val ARG_TEAM = "team"
    }

    private lateinit var binding: FragmentTeamBinding

    private val args: TeamFragmentArgs by navArgs()

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setTitle()

        matchViewModel.matchLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                loadTeam(it)
            }
        })
    }

    private fun setTitle() {
        (requireActivity() as MainActivity).supportActionBar?.title = args.team.name
    }

    private fun loadTeam(match: Match) {

    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

}