package com.example.alf.ui.match.events.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.event.LiveEventType
import com.example.alf.databinding.FragmentLiveEventTypesBinding
import com.google.android.material.snackbar.Snackbar

class LiveEventTypesFragment : Fragment(), LiveEventTypesAdapter.LiveEventTypesListener {

    private lateinit var binding: FragmentLiveEventTypesBinding

    private lateinit var viewAdapter: LiveEventTypesAdapter

    private val args: LiveEventTypesFragmentArgs by navArgs()

    private val liveEventTypesViewModel: LiveEventTypesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveEventTypesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewAdapter = LiveEventTypesAdapter(this)
        binding.liveEventTypesRecyclerView.apply {
            adapter = viewAdapter
        }

        liveEventTypesViewModel.liveEventTypesLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.liveEventTypesRecyclerView.visibility = View.VISIBLE
                viewAdapter.setLiveEventTypes(liveEventTypesViewModel.liveEventTypesLiveData.value!!)

                binding.progressBar.visibility = View.GONE
            } else {
                showSnackBar(binding.root, "Something went wrong")
            }
        })
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(liveEventType: LiveEventType) {
        val action = liveEventType.id.let { LiveEventTypesFragmentDirections.actionLiveEventTypesFragmentToLiveEventFragment(
                matchId = args.matchId,
                liveEventTypeId = it,
                liveEventType = liveEventType
        ) }
        findNavController().navigate(action)
    }
}