package com.example.alf.ui.match.referees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alf.data.model.Referee
import com.example.alf.databinding.FragmentMatchRefereesBinding
import com.google.android.material.snackbar.Snackbar


class MatchRefereesFragment : Fragment(), MatchRefereesAdapter.MatchRefereeListener {

    companion object {
        const val ARG_MATCH_ID = "matchId"
    }

    private lateinit var binding: FragmentMatchRefereesBinding

    private lateinit var viewAdapter: MatchRefereesAdapter

    private val matchRefereesViewModel: MatchRefereesViewModel by viewModels {
        MatchRefereesViewModelFactory(
                requireActivity().application,
                requireArguments().getInt(ARG_MATCH_ID)
        )
    }

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

        matchRefereesViewModel.matchRefereesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                viewAdapter = MatchRefereesAdapter(this)
                viewAdapter.setReferees(it)
                binding.matchPersonsRecyclerView.apply {
                    adapter = viewAdapter
                }
            } else {
                showSnackBar(binding.root, "Get match referees failed")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(referee: Referee, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(referee: Referee) {
        TODO("Not yet implemented")
    }

}