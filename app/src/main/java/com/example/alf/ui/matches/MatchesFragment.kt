package com.example.alf.ui.matches

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
import com.example.alf.data.model.MatchModel
import com.example.alf.ui.persons.PersonsFragmentDirections

class MatchesFragment : Fragment(), MatchesAdapter.MatchListener {

    private lateinit var matchesViewModel: MatchesViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MatchesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        matchesViewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_matches, container, false)

        progressBar = root.findViewById(R.id.matches_progress)
        recyclerView = root.findViewById(R.id.matches_recycler_view)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchesViewModel.fetchAllMatches()
        matchesViewModel.matchModelListLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                recyclerView.visibility = View.VISIBLE
                viewAdapter.setMatches(it as ArrayList<MatchModel>)
            } else {
                showToast("Something went wrong")
            }
            progressBar.visibility = View.GONE
        })

        viewAdapter = MatchesAdapter(this)
        viewManager = LinearLayoutManager(context)
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

    override fun onItemDeleted(matchModel: MatchModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchModel: MatchModel, position: Int) {
        val action = matchModel.id.let { MatchesFragmentDirections.actionNavMatchesToMatchFragment(matchId = it) }
        findNavController().navigate(action)
    }
}