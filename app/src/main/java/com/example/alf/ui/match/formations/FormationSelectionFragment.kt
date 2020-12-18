package com.example.alf.ui.match.formations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alf.data.model.match.Formation
import com.example.alf.databinding.FragmentFormationSelectionBinding
import com.google.android.material.snackbar.Snackbar


class FormationSelectionFragment : Fragment(), FormationsAdapter.FormationsListener {

    private lateinit var binding: FragmentFormationSelectionBinding

    private val args: FormationSelectionFragmentArgs by navArgs()

    private val formationsViewModel: FormationsViewModel by viewModels {
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
        binding.formationsViewModel = formationsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewAdapter = FormationsAdapter(this)
        binding.formationsRecyclerView.apply {
            adapter = viewAdapter
        }

        formationsViewModel.formationsLiveData.observe(viewLifecycleOwner, {
            onGetFormationsResult(it)
        })

    }

    private fun onGetFormationsResult(formations: List<Formation>?) {
        if (formations != null) {
            viewAdapter.setFormations(formations)
        } else {
            showSnackBar(binding.root, "Get available formations failed")
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(formation: Formation) {
        TODO("Not yet implemented")
    }
}