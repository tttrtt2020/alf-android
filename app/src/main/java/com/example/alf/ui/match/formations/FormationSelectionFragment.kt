package com.example.alf.ui.match.formations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.match.Formation
import com.example.alf.databinding.FragmentFormationSelectionBinding
import com.google.android.material.snackbar.Snackbar


class FormationSelectionFragment : Fragment(), FormationsAdapter.FormationsListener {

    private lateinit var binding: FragmentFormationSelectionBinding

    private val args: FormationSelectionFragmentArgs by navArgs()

    private val formationSelectionViewModel: FormationSelectionViewModel by viewModels {
        FormationsViewModelFactory(
                args.matchId,
                args.teamId
        )
    }

    private lateinit var viewAdapter: FormationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormationSelectionBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.formationSelectionViewModel = formationSelectionViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeFormationSelectionViewModel()
    }

    private fun setupViews() {
        binding.retryButton.setOnClickListener { getFormations() }
    }

    private fun observeFormationSelectionViewModel() {
        formationSelectionViewModel.formationsLiveData.observe(viewLifecycleOwner, {
            onGetFormationsResult(it)
        })

        formationSelectionViewModel.addFormationToMatchLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onSetFormationResult(it)
                formationSelectionViewModel.addFormationToMatchLiveData.value = null
            }
        }
    }

    private fun getFormations() {
        formationSelectionViewModel.getFormations()
    }

    private fun onGetFormationsResult(formations: List<Formation>?) {
        if (formations != null) {
            viewAdapter = FormationsAdapter(formations, this)
            binding.formationsRecyclerView.adapter = viewAdapter
        } else {
            showSnackBar(binding.root, "Get available formations failed")
        }
    }

    private fun onSetFormationResult(success: Boolean) {
        if (success) {
            showSnackBar(binding.root, "Set formation success")
            goBack()
        } else showSnackBar(binding.root, "Set formation failed")
    }

    private fun goBack() {
        val action = FormationSelectionFragmentDirections.actionFormationSelectionFragmentToTeamFragment(
                args.matchId, args.teamId
        )
        findNavController().navigate(action)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(formation: Formation) {
        selectFormation(formation)
    }

    private fun selectFormation(formation: Formation) {
        formationSelectionViewModel.setFormation(formation)
    }
}