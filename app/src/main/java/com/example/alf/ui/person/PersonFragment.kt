package com.example.alf.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.databinding.FragmentMatchBinding
import com.example.alf.databinding.FragmentPersonBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.persons.PersonsPagingAdapter

class PersonFragment : Fragment() {

    private lateinit var binding: FragmentPersonBinding

    private val args: PersonFragmentArgs by navArgs()

    private val personViewModel: PersonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personViewModel.fetchPersonById(args.personId)
        personViewModel.personModelLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.firstName.setText(it.firstName)
                binding.patronymic.setText(it.patronymic)
                binding.lastName.setText(it.lastName)
                if (it.birthDate != null) {
                    binding.birthDate.updateDate(
                            it.birthDate.year,
                            it.birthDate.month,
                            it.birthDate.day
                    )
                }
                binding.country.setText(it.country?.name)
                binding.height.setText(it.height.toString())
                binding.weight.setText(it.weight.toString())
                // load photo
                val photoImageUrl = PersonsPagingAdapter.personsImagesUrl + it.id + PersonsPagingAdapter.personsImagesExtension
                context?.let { it1 ->
                    Glide
                            .with(it1)
                            .load(photoImageUrl)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.holo_red_dark)
                            .into(binding.photo)
                }
            } else {
                showToast("Something went wrong")
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}