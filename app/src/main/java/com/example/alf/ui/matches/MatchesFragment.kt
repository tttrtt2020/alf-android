package com.example.alf.ui.matches

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alf.R
import com.example.alf.data.model.Match
import com.example.alf.databinding.FragmentMatchesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MatchesFragment : Fragment(), MatchesPagingAdapter.MatchListener {

    private lateinit var binding: FragmentMatchesBinding

    private val matchesViewModel: MatchesViewModel by viewModels()

    private lateinit var viewAdapter: MatchesPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(layoutInflater)
        /*binding.lifecycleOwner = this
        binding.matchesViewModel = matchesViewModel*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = MatchesPagingAdapter(MatchesPagingAdapter.MatchComparator, this)
        binding.matchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        showMatches(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.matches, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_only_finished -> {
                showMatches(true)
                true
            }
            /*R.id.help -> {
                showHelp()
                true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMatches(showOnlyFinished: Boolean) {
        matchesViewModel.loadingInProgressLiveData.value = true
        viewLifecycleOwner.lifecycleScope.launch {
            matchesViewModel.flow.collectLatest { pagingData ->
                matchesViewModel.loadingInProgressLiveData.value = false
                viewAdapter.submitData(pagingData)
            }
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(match: Match, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(match: Match) {
        val action = match.id.let { MatchesFragmentDirections.actionNavMatchesToMatchFragment(
                matchId = it,
                hostTeamId = match.hostTeam.id,
                guestTeamId = match.guestTeam.id
        ) }
        findNavController().navigate(action)
    }

}