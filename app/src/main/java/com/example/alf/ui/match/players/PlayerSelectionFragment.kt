package com.example.alf.ui.match.players

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
import com.example.alf.data.model.Player
import com.example.alf.databinding.FragmentPlayerSelectionBinding
import com.example.alf.ui.PlayersLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class PlayerSelectionFragment : Fragment(), SearchView.OnQueryTextListener, PlayersPagingAdapter.PlayerListener {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    private lateinit var binding: FragmentPlayerSelectionBinding

    private val args: PlayerSelectionFragmentArgs by navArgs()

    private lateinit var playerSelectionViewModel: SearchPlayersViewModel

    private val viewAdapter = PlayersPagingAdapter(PlayersPagingAdapter.PlayerComparator, this)

    private lateinit var searchView: SearchView
    private var searchJob: Job? = null

    private val sort = AlfApplication.getProperty("players.sort")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerSelectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        playerSelectionViewModel = ViewModelProvider(this, Injection.providePlayersViewModelFactory(args.matchId, args.teamId)).get(
                SearchPlayersViewModel::class.java
        )
        binding.playerSelectionViewModel = playerSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        initSearch(query)
        search(query)

        observePlayerSelectionViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.player_selection, menu)

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
        binding.playersRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = viewAdapter.withLoadStateHeaderAndFooter(
                    header = PlayersLoadStateAdapter { viewAdapter.retry() },
                    footer = PlayersLoadStateAdapter { viewAdapter.retry() }
            )
        }
    }

    private fun initAdapter() {
        viewAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.playersRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
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

    private fun observePlayerSelectionViewModel() {
        playerSelectionViewModel.addPlayerToMatchLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onAddMatchPlayerResult(it)
                playerSelectionViewModel.addPlayerToMatchLiveData.value = null
            }
        }
    }

    override fun onItemClick(player: Player) {
        selectPlayer(player)
    }

    private fun selectPlayer(player: Player) {
        playerSelectionViewModel.addPlayerToMatch(
                args.matchId, args.teamId, args.fieldPosition?.id,
                player
        )
    }

    private fun onAddMatchPlayerResult(success: Boolean) {
        if (success) {
            showSnackBar(binding.root, "Add match player success")
            goBack()
        } else showSnackBar(binding.root, "Add match player failed")
    }

    private fun goBack() {
        val action = PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToTeamFragment(
                args.matchId, args.teamId
        )
        findNavController().navigate(action)
    }


    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            playerSelectionViewModel.searchPlayers(query, sort).collectLatest {
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
                .collect { binding.playersRecyclerView.scrollToPosition(0) }
        }
    }

    private fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.playersRecyclerView.scrollToPosition(0)
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