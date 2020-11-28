package com.example.alf.ui.match.events.live

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.alf.R
import com.example.alf.databinding.FragmentLiveEventBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.MatchViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LiveEventFragment : Fragment() {

    companion object {
        fun newInstance() = LiveEventFragment()

        const val MAX_MINUTE: Int = 90
    }

    private lateinit var binding: FragmentLiveEventBinding

    private val args: LiveEventFragmentArgs by navArgs()

    private val liveEventViewModel: LiveEventViewModel by viewModels {
        LiveEventViewModelFactory(args.liveEventTypeId)
    }
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment) {
        MatchViewModelFactory(activity?.application!!, args.matchId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLiveEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.liveEventViewModel = liveEventViewModel
        binding.matchViewModel = matchViewModel
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.live_event, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        liveEventViewModel.saveLiveEvent()
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

}