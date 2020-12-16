package com.example.alf.ui.persons

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
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.AlfApplication
import com.example.alf.Injection
import com.example.alf.R
import com.example.alf.data.model.Person
import com.example.alf.databinding.FragmentPersonsBinding
import com.example.alf.ui.PersonsLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class PersonsFragment : Fragment(), SearchView.OnQueryTextListener, PersonsPagingAdapter.PersonListener {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    private lateinit var binding: FragmentPersonsBinding

    private lateinit var personsViewModel: SearchPersonsViewModel

    private val viewAdapter = PersonsPagingAdapter(PersonsPagingAdapter.PersonComparator, this)

    private lateinit var searchView: SearchView
    private var searchJob: Job? = null

    private val sort = AlfApplication.getProperty("persons.sort")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        //binding.personsViewModel = personsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the view model
        personsViewModel = ViewModelProvider(this, Injection.providePersonsViewModelFactory()).get(
            SearchPersonsViewModel::class.java
        )

        setupFab()
        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.persons, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener { openCreateNewPerson() }
        binding.personsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.fab.hide()
                } else {
                    binding.fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putString(LAST_SEARCH_QUERY, binding.searchRepo.text.trim().toString())
    }

    override fun onItemDeleted(person: Person, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(person: Person) {
        openPerson(person)
    }

    private fun openPerson(person: Person) {
        val action = person.id.let { PersonsFragmentDirections.actionNavPersonsToPersonFragment(
                personId = it,
                person = person
        ) }
        findNavController().navigate(action)
    }

    private fun openCreateNewPerson() {
        val action = PersonsFragmentDirections.actionNavPersonsToPersonFragment(
                personId = 0,
                person = null
        )
        findNavController().navigate(action)
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            personsViewModel.searchPersons(query, sort).collectLatest {
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

    private fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.personsRecyclerView.scrollToPosition(0)
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