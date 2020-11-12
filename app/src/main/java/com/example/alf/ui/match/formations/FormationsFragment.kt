package com.example.alf.ui.match.formations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alf.data.model.match.FormationModel
import com.example.alf.databinding.FragmentFormationsBinding


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
        formationsViewModel.formationPersonsModelLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.formationsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setFormations(it as ArrayList<FormationModel>)

                binding.progressBar.visibility = View.GONE
            } else {
                showToast("Something went wrong")
            }
        })

    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(formationModel: FormationModel, position: Int) {
        TODO("Not yet implemented")
    }
}