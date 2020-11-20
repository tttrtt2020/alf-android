package com.example.alf.ui.persons

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

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

        binding.fab.setOnClickListener {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // get the view model
        personsViewModel = ViewModelProvider(this, Injection.provideViewModelFactory()).get(
            SearchPersonsViewModel::class.java
        )

        initFab()
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

    private fun initFab() {
        binding.fab.setOnClickListener { fab ->
            /*Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()*/
            openCreateNewPerson()
        }
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

    override fun onItemDeleted(person: Person, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(person: Person, position: Int) {
        openPerson(person)
    }

    private fun openPerson(person: Person) {
        val action = person.id.let { PersonsFragmentDirections.actionNavPersonsToPersonFragment(
            personId = it
        ) }
        findNavController().navigate(action)
    }

    private fun openCreateNewPerson() {
        val action = PersonsFragmentDirections.actionNavPersonsToPersonFragment(
            personId = 0
        )
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

    private fun doSearch(query: String) {
        query.trim().let {
            if (it.isNotEmpty()) {
                binding.personsRecyclerView.scrollToPosition(0)
                search(it)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            doSearch(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }
}