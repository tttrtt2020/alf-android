package com.example.alf.ui.matches

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alf.R
import com.example.alf.data.model.Match
import com.example.alf.databinding.FragmentMatchesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MatchesFragment : Fragment(), MatchesPagingAdapter.MatchListener {

    private lateinit var binding: FragmentMatchesBinding

    private val matchesViewModel by viewModels<MatchesViewModel>()

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
        viewLifecycleOwner.lifecycleScope.launch {
            matchesViewModel.flow.collectLatest { pagingData ->
                viewAdapter.submitData(pagingData)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(match: Match, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(match: Match, position: Int) {
        val action = match.id.let { MatchesFragmentDirections.actionNavMatchesToMatchFragment(matchId = it) }
        findNavController().navigate(action)
    }


}