package com.example.alf.ui.match.statuses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alf.data.model.match.Status
import com.example.alf.databinding.FragmentStatusSelectionBinding
import com.google.android.material.snackbar.Snackbar


class StatusSelectionFragment : Fragment(), StatusesAdapter.StatusesListener {

    private lateinit var binding: FragmentStatusSelectionBinding

    private val args: StatusSelectionFragmentArgs by navArgs()

    private val statusSelectionViewModel: StatusSelectionViewModel by viewModels {
        StatusesViewModelFactory(
                args.matchId
        )
    }

    private lateinit var viewAdapter: StatusesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusSelectionBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.statusSelectionViewModel = statusSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeStatusSelectionViewModel()
    }

    private fun setupViews() {
        binding.statusesRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.retryButton.setOnClickListener { getStatuses() }
    }

    private fun observeStatusSelectionViewModel() {
        statusSelectionViewModel.statusesLiveData.observe(viewLifecycleOwner, {
            onGetStatusesResult(it)
        })

        statusSelectionViewModel.setStatusToMatchLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onSetStatusResult(it)
                statusSelectionViewModel.setStatusToMatchLiveData.value = null
            }
        }
    }

    private fun getStatuses() {
        statusSelectionViewModel.getStatuses()
    }

    private fun onGetStatusesResult(statuses: List<Status>?) {
        statuses?.let {
            viewAdapter = StatusesAdapter(statuses, this)
            binding.statusesRecyclerView.adapter = viewAdapter
        }
    }

    private fun onSetStatusResult(success: Boolean) {
        if (success) {
            showSnackBar(binding.root, "Set status success")
            goBack()
        } else showSnackBar(binding.root, "Set status failed")
    }

    private fun goBack() {
        val action = StatusSelectionFragmentDirections.actionStatusSelectionFragmentToMatchFragment(
                args.matchId, args.hostTeamId, args.guestTeamId
        )
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(status: Status) {
        selectStatus(status)
    }

    private fun selectStatus(status: Status) {
        statusSelectionViewModel.setStatus(status)
    }

}