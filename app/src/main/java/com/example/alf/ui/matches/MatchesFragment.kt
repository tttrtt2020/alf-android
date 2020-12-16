package com.example.alf.ui.matches

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.alf.AlfApplication
import com.example.alf.Injection
import com.example.alf.R
import com.example.alf.data.model.Match
import com.example.alf.databinding.FragmentMatchesBinding
import com.example.alf.ui.MatchesLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MatchesFragment : Fragment(), MatchesPagingAdapter.MatchListener, SearchView.OnQueryTextListener {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    private lateinit var binding: FragmentMatchesBinding

    private lateinit var matchesViewModel: MatchesViewModel

    private val viewAdapter = MatchesPagingAdapter(MatchesPagingAdapter.MatchComparator, this)

    private lateinit var searchView: SearchView
    private var searchJob: Job? = null

    private val sort = AlfApplication.getProperty("matches.sort")

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

        // get the view model
        matchesViewModel = ViewModelProvider(this, Injection.provideMatchesViewModelFactory()).get(
                MatchesViewModel::class.java
        )

        initAdapter()
        val query = savedInstanceState?.getString(MatchesFragment.LAST_SEARCH_QUERY) ?: MatchesFragment.DEFAULT_QUERY
        search(query)
        initSearch(query)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.matches, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_only_finished -> {
                TODO("Not yet implemented")
            }
            /*R.id.help -> {
                showHelp()
                true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            matchesViewModel.searchMatches(query, sort).collectLatest {
                viewAdapter.submitData(it)
            }
        }
    }

    private fun initSearch(query: String) {
        //...
        // First part of the method is unchanged

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            viewAdapter.loadStateFlow
                    // Only emit when REFRESH LoadState changes.
                    .distinctUntilChangedBy { it.refresh }
                    // Only react to cases where REFRESH completes i.e., NotLoading.
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.matchesRecyclerView.scrollToPosition(0) }
        }
    }

    private fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.matchesRecyclerView.scrollToPosition(0)
                search(it)
            }
        }
    }

    private fun initAdapter() {
        binding.matchesRecyclerView.adapter = viewAdapter.withLoadStateHeaderAndFooter(
                header = MatchesLoadStateAdapter { viewAdapter.retry() },
                footer = MatchesLoadStateAdapter { viewAdapter.retry() }
        )
        viewAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.matchesRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Show exception on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                showSnackBar(binding.root, it.error.stackTraceToString())
            }
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(match: Match) {
        val action = match.id.let { MatchesFragmentDirections.actionNavMatchesToMatchFragment(
                matchId = it,
                hostTeamId = match.hostTeam.id,
                guestTeamId = match.guestTeam.id
        ) }
        findNavController().navigate(action)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { doSearch(it) }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let { doSearch(it) }
        return true
    }

}