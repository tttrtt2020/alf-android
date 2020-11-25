package com.example.alf.ui.match.squad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.databinding.FragmentSquadBinding
import com.example.alf.ui.match.MatchFragmentDirections
import com.example.alf.ui.match.MatchInfoAdapter
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.formations.TeamFormationView
import com.google.android.material.snackbar.Snackbar


class SquadFragment : Fragment(), MatchPersonsAdapter.SquadListener, TeamFormationView.OnChangeFormationClickListener {

    private lateinit var binding: FragmentSquadBinding

    private lateinit var viewAdapter: MatchPersonsAdapter

    private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })

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

        getSquadLiveData().observe(viewLifecycleOwner, {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(getSquadLiveData().value!!)

                binding.progressBar.visibility = View.GONE

                binding.teamFormation.setMatchPersons(it)
            } else {
                showSnackBar(binding.root, "Get squad error")
            }
        })
    }

    private fun getSquadLiveData(): LiveData<List<MatchPerson>>{
        return when {
            requireArguments().get(MatchInfoAdapter.ARG_TEAM) == MatchInfoAdapter.ARG_TEAM_HOST ->
                matchViewModel.hostSquadLiveData
            requireArguments().get(MatchInfoAdapter.ARG_TEAM) == MatchInfoAdapter.ARG_TEAM_GUEST ->
                matchViewModel.guestSquadLiveData
            else -> throw IllegalArgumentException(
                MatchInfoAdapter.ARG_TEAM
                        + " argument should have "
                        + MatchInfoAdapter.ARG_TEAM_HOST
                        + " or "
                        + MatchInfoAdapter.ARG_TEAM_GUEST + " value"
            )
        }
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