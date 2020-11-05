package com.example.alf.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.PersonModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PersonsFragment : Fragment(), PersonsPagingAdapter.PersonListener {

    private val personsViewModel by viewModels<PersonsViewModel>()

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PersonsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_persons, container, false)
        progressBar = root.findViewById(R.id.persons_progress)
        recyclerView = root.findViewById(R.id.persons_recycler_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = PersonsPagingAdapter(PersonsPagingAdapter.PersonModelComparator, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            personsViewModel.flow.collectLatest { pagingData ->
                viewAdapter.submitData(pagingData)
            }
        }
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

    fun search(query: String) {
        TODO("Not yet implemented")
    }
}