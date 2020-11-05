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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.ui.persons.PersonsAdapter

class PersonFragment : Fragment() {

    private lateinit var personViewModel: PersonViewModel

    private lateinit var photoImageView: ImageView
    private lateinit var firstNameTextView: TextView
    private lateinit var patronymicTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var birthDateDatePicker: DatePicker
    private lateinit var countryTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView

    private val args: PersonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personViewModel =
            ViewModelProvider(this).get(PersonViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_person, container, false)

        photoImageView = root.findViewById(R.id.photo)
        firstNameTextView = root.findViewById(R.id.first_name)
        patronymicTextView = root.findViewById(R.id.patronymic)
        lastNameTextView = root.findViewById(R.id.last_name)
        birthDateDatePicker = root.findViewById(R.id.birth_date)
        countryTextView = root.findViewById(R.id.country)
        heightTextView = root.findViewById(R.id.height)
        weightTextView = root.findViewById(R.id.weight)

        personViewModel = ViewModelProvider(this)[PersonViewModel::class.java]
        personViewModel.fetchPersonById(args.personId)
        personViewModel.personModelLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                firstNameTextView.text = it.firstName
                patronymicTextView.text = it.patronymic
                lastNameTextView.text = it.lastName
                if (it.birthDate != null) {
                    birthDateDatePicker.updateDate(
                        it.birthDate.year,
                        it.birthDate.month,
                        it.birthDate.day
                    )
                }
                countryTextView.text = it.country?.name
                heightTextView.text = it.height.toString()
                weightTextView.text = it.weight.toString()
                // load photo
                val photoImageUrl = PersonsAdapter.personsImagesUrl + it.id + PersonsAdapter.personsImagesExtension
                context?.let { it1 ->
                    Glide
                        .with(it1)
                        .load(photoImageUrl)
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.holo_red_dark)
                        .into(photoImageView)
                }
            } else {
                showToast("Something went wrong")
            }
        })

        return root
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}