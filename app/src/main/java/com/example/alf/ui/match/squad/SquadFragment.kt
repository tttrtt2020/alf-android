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
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.databinding.FragmentSquadBinding
import com.example.alf.ui.match.MatchFragmentDirections
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.formations.TeamFormationView
import com.google.android.material.snackbar.Snackbar


class SquadFragment : Fragment(), MatchPersonsAdapter.SquadListener, TeamFormationView.OnChangeFormationClickListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
        const val ARG_TEAM_SIDE = "teamSide"
        const val ARG_TEAM_SIDE_HOST = "host"
        const val ARG_TEAM_SIDE_GUEST = "guest"
        const val ARG_TEAM_ID = "teamId"
    }

    private lateinit var binding: FragmentSquadBinding

    private lateinit var viewAdapter: MatchPersonsAdapter

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)
    private val squadViewModel: SquadViewModel by viewModels {
        SquadViewModelFactory(
                requireActivity().application!!,
                requireArguments().getInt(ARG_MATCH_ID),
                requireArguments().getInt(ARG_TEAM_ID)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSquadBinding.inflate(layoutInflater)

        binding.teamFormation.setOnChangeFormationClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = MatchPersonsAdapter(this)
        binding.matchPersonsRecyclerView.apply {
            adapter = viewAdapter
        }

        squadViewModel.squadLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(squadViewModel.squadLiveData.value!!)

                binding.progressBar.visibility = View.GONE
            } else {
                showSnackBar(binding.root, "Get squad failed")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPerson: MatchPerson, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchPerson: MatchPerson, position: Int) {
        TODO("Not yet implemented")
    }

    override fun setOnChangeFormationClickListener() {
        val matchId = matchViewModel.matchLiveData.value?.id
        val action = matchId.let { MatchFragmentDirections.actionMatchFragmentToFormationsFragment() }
        findNavController().navigate(action)
    }
}