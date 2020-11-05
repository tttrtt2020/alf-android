package com.example.alf.ui.matches

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
import com.example.alf.data.model.MatchModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MatchesFragment : Fragment(), MatchesPagingAdapter.MatchListener {

    private val matchesViewModel by viewModels<MatchesViewModel>()

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MatchesPagingAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_matches, container, false)
        progressBar = root.findViewById(R.id.matches_progress)
        recyclerView = root.findViewById(R.id.matches_recycler_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = MatchesPagingAdapter(MatchesPagingAdapter.MatchModelComparator, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            matchesViewModel.flow.collectLatest { pagingData ->
                viewAdapter.submitData(pagingData)
            }
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