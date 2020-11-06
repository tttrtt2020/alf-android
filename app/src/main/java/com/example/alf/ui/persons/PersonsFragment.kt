package com.example.alf.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.alf.Injection
import com.example.alf.data.model.PersonModel
import com.example.alf.databinding.FragmentPersonsBinding
import com.example.alf.ui.PersonsLoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class PersonsFragment : Fragment(), PersonsPagingAdapter.PersonListener {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    private lateinit var binding: FragmentPersonsBinding
    private lateinit var personsViewModel: SearchPersonsViewModel
    private val viewAdapter = PersonsPagingAdapter(PersonsPagingAdapter.PersonModelComparator, this)

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the view model
        personsViewModel = ViewModelProvider(this, Injection.provideViewModelFactory()).get(SearchPersonsViewModel::class.java)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
    }

    private fun initAdapter() {
        binding.personsRecyclerView.adapter = viewAdapter.withLoadStateHeaderAndFooter(
            header = PersonsLoadStateAdapter { viewAdapter.retry() },
            footer = PersonsLoadStateAdapter { viewAdapter.retry() }
        )
        viewAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.personsRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text.trim().toString())
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(personModel: PersonModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(personModel: PersonModel, position: Int) {
        val action = personModel.id.let { PersonsFragmentDirections.actionNavPersonsToPersonFragment(personId = it) }
        findNavController().navigate(action)
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            personsViewModel.searchPersons(query).collectLatest {
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
                .collect { binding.personsRecyclerView.scrollToPosition(0) }
        }
    }

    fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.personsRecyclerView.scrollToPosition(0)
                search(it)
            }
        }
    }
}