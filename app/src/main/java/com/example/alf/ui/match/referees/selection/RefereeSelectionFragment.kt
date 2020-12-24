package com.example.alf.ui.match.referees.selection

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
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alf.AlfApplication
import com.example.alf.Injection
import com.example.alf.R
import com.example.alf.data.model.Referee
import com.example.alf.databinding.FragmentRefereeSelectionBinding
import com.example.alf.ui.RefereesLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class RefereeSelectionFragment : Fragment(), SearchView.OnQueryTextListener, RefereesPagingAdapter.RefereeListener {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    private lateinit var binding: FragmentRefereeSelectionBinding

    private val args: RefereeSelectionFragmentArgs by navArgs()

    private lateinit var refereeSelectionViewModel: RefereeSelectionViewModel

    private val viewAdapter = RefereesPagingAdapter(RefereesPagingAdapter.RefereeComparator, this)

    private lateinit var searchView: SearchView
    private var searchJob: Job? = null

    private val sort = AlfApplication.getProperty("referees.sort")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRefereeSelectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        refereeSelectionViewModel = ViewModelProvider(
                this,
                Injection.provideRefereesViewModelFactory(args.matchId)
        ).get(RefereeSelectionViewModel::class.java)
        binding.refereeSelectionViewModel = refereeSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        initSearch(query)
        search(query)

        observeRefereeSelectionViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.referee_selection, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text.trim().toString())
    }

    private fun setupViews() {
        binding.refereesRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = viewAdapter.withLoadStateHeaderAndFooter(
                    header = RefereesLoadStateAdapter { viewAdapter.retry() },
                    footer = RefereesLoadStateAdapter { viewAdapter.retry() }
            )
        }
    }

    private fun initAdapter() {
        viewAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.refereesRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
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

    private fun observeRefereeSelectionViewModel() {
        refereeSelectionViewModel.addRefereeToMatchLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onAddMatchRefereeResult(it)
                refereeSelectionViewModel.addRefereeToMatchLiveData.value = null
            }
        }
    }

    override fun onItemClick(referee: Referee) {
        selectReferee(referee)
    }

    private fun selectReferee(referee: Referee) {
        refereeSelectionViewModel.addRefereeToMatch(
            args.matchId,
            referee
        )
    }

    private fun onAddMatchRefereeResult(success: Boolean) {
        if (success) {
            //showSnackBar(binding.root, "Add match referee success")
            goBack()
        } else showSnackBar(binding.root, "Add match referee failed")
    }

    private fun goBack() {
        val action = RefereeSelectionFragmentDirections.actionRefereeSelectionFragmentToMatchRefereesFragment(
                args.matchId
        )
        findNavController().navigate(action)
    }


    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            refereeSelectionViewModel.searchReferees(query, sort).collectLatest {
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
                .collect { binding.refereesRecyclerView.scrollToPosition(0) }
        }
    }

    private fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.refereesRecyclerView.scrollToPosition(0)
                search(it)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { doSearch(it) }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let { doSearch(it) }
        return true
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }
}