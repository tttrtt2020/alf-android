package com.example.alf.ui.match.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.alf.R
import com.example.alf.data.model.event.Event
import com.example.alf.databinding.FragmentEventsBinding
import com.example.alf.ui.match.MatchViewModel


class EventsFragment : Fragment(), EventsAdapter.EventsListener {

    private lateinit var binding: FragmentEventsBinding

    private lateinit var viewAdapter: EventsAdapter

    //private val matchViewModel: MatchViewModel by viewModels({ requireParentFragment() })
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = EventsAdapter(this)
        binding.eventsRecyclerView.apply {
            adapter = viewAdapter
        }

        matchViewModel.eventsLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.eventsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setEvents(matchViewModel.eventsLiveData.value!!)

                binding.progressBar.visibility = View.GONE
            } else {
                showToast("Something went wrong")
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(event: Event, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(event: Event, position: Int) {
        TODO("Not yet implemented")
    }
}