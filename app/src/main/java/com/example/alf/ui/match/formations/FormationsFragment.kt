package com.example.alf.ui.match.formations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alf.data.model.match.Formation
import com.example.alf.databinding.FragmentFormationsBinding
import com.google.android.material.snackbar.Snackbar


class FormationsFragment : Fragment(), FormationsAdapter.FormationsListener {

    private lateinit var binding: FragmentFormationsBinding

    private lateinit var viewAdapter: FormationsAdapter

    private val formationsViewModel by viewModels<FormationsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormationsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewAdapter = FormationsAdapter(this)
        binding.formationsRecyclerView.apply {
            adapter = viewAdapter
        }

        formationsViewModel.fetchFormations()
        formationsViewModel.formationPersonsLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.formationsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setFormations(it as ArrayList<Formation>)

                binding.progressBar.visibility = View.GONE
            } else {
                showSnackBar(view, "Something went wrong")
            }
        })

    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemClick(formation: Formation) {
        TODO("Not yet implemented")
    }
}