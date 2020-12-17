package com.example.alf.ui.match.fieldPositions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.databinding.FragmentFieldPositionSelectionBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldPositionSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewAdapter = FieldPositionsAdapter(this)
        binding.fieldPositionsRecyclerView.apply {
            adapter = viewAdapter
        }

        fieldPositionsViewModel.fieldPositionsLiveData.observe(viewLifecycleOwner, {
            onGetFieldPositionsResult(it)
        })

    }

    private fun onGetFieldPositionsResult(fieldPositions: List<FieldPosition>?) {
        if (fieldPositions != null) {
            viewAdapter.setFieldPositions(fieldPositions)
        } else {
            showSnackBar(binding.root, "Get available field positions failed")
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(fieldPosition: FieldPosition) {
        TODO("Not yet implemented")
    }
}