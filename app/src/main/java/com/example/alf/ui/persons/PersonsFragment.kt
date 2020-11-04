package com.example.alf.ui.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.PersonModel

class PersonsFragment : Fragment(), PersonsAdapter.PersonListener {

    private lateinit var personsViewModel: PersonsViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PersonsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personsViewModel = ViewModelProvider(this).get(PersonsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_persons, container, false)

        progressBar = root.findViewById(R.id.persons_progress)
        recyclerView = root.findViewById(R.id.persons_recycler_view)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //personsViewModel = ViewModelProvider(this)[PersonsViewModel::class.java]
        //personsViewModel.fetchAllPersons()
        personsViewModel.fetchPersonsByQuery("")
        personsViewModel.personModelListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                recyclerView.visibility = View.VISIBLE
                viewAdapter.setPersons(it as ArrayList<PersonModel>)
            } else {
                showToast("Something went wrong")
            }
            progressBar.visibility = View.GONE
        })

        viewAdapter = PersonsAdapter(this)
        viewManager = LinearLayoutManager(context);
        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(personModel: PersonModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(personModel: PersonModel, position: Int) {
        val action = personModel.id?.let { PersonsFragmentDirections.actionNavPersonsToPersonFragment(personId = it) }
        if (action != null) {
            findNavController().navigate(action)
        }
    }

    fun search(query: String) {
        personsViewModel.fetchPersonsByQuery(query)
        personsViewModel.personModelListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                recyclerView.visibility = View.VISIBLE
                viewAdapter.setPersons(it as ArrayList<PersonModel>)
            } else {
                showToast("Something went wrong")
            }
            progressBar.visibility = View.GONE
        })
    }
}