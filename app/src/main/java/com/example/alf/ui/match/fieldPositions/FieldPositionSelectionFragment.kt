package com.example.alf.ui.match.fieldPositions

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alf.R
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.databinding.FragmentFieldPositionSelectionBinding
import com.example.alf.ui.match.players.selection.Mode
import com.google.android.material.snackbar.Snackbar


class FieldPositionSelectionFragment : Fragment(), FieldPositionsAdapter.FieldPositionsListener {

    private lateinit var binding: FragmentFieldPositionSelectionBinding

    private val args: FieldPositionSelectionFragmentArgs by navArgs()

    private val fieldPositionsViewModel: FieldPositionsViewModel by viewModels {
        FieldPositionsViewModelFactory(
                args.matchId,
                args.teamId
        )
    }

    private lateinit var viewAdapter: FieldPositionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldPositionSelectionBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.fieldPositionSelectionViewModel = fieldPositionsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeFieldPositionSelectionViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.field_position_selection, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_skip -> {
                openPlayerSelectionFragment(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        binding.fieldPositionsRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.retryButton.setOnClickListener { getFieldPositions() }
    }

    private fun observeFieldPositionSelectionViewModel() {
        fieldPositionsViewModel.fieldPositionsLiveData.observe(viewLifecycleOwner, {
            onGetFieldPositionsResult(it)
        })
    }

    private fun getFieldPositions() {
        fieldPositionsViewModel.getFieldPositions()
    }

    private fun onGetFieldPositionsResult(fieldPositions: List<FieldPosition>?) {
        fieldPositions?.let {
            viewAdapter = FieldPositionsAdapter(fieldPositions, this)
            binding.fieldPositionsRecyclerView.adapter = viewAdapter
        }
    }

    private fun openPlayerSelectionFragment(fieldPosition: FieldPosition?) {
        val action = FieldPositionSelectionFragmentDirections.actionFieldPositionSelectionFragmentToPlayerSelectionFragment(
                matchId = args.matchId,
                hostTeamId = 0, guestTeamId = 0,
                minute = 0,
                teamId = args.teamId, team = null,
                fieldPosition = fieldPosition,
                eventType = null,
                playerOut = null,
                mode = Mode.TEAM_PLAYER
        )
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(fieldPosition: FieldPosition) {
        openPlayerSelectionFragment(fieldPosition)
    }
}