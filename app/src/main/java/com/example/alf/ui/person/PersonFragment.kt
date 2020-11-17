package com.example.alf.ui.person

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.data.model.PersonModel
import com.example.alf.databinding.FragmentPersonBinding
import com.example.alf.ui.persons.PersonsPagingAdapter
import java.text.SimpleDateFormat
import java.util.*


class PersonFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentPersonBinding

    private val args: PersonFragmentArgs by navArgs()

    private val personViewModel: PersonViewModel by viewModels { PersonViewModelFactory(activity?.application!!, args.personId) }

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private lateinit var saveMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

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

        personViewModel.getPersonLiveData().observe(viewLifecycleOwner, {
            if (it != null) {
                onPersonLoadSuccess(it)
            } else {
                onPersonLoadFail()
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.person, menu)

        saveMenuItem = menu.findItem(R.id.action_save)
        personViewModel.saveEnabledLiveData.observe(viewLifecycleOwner, {
            saveMenuItem.isEnabled = it
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_photo -> {
                takePhoto()
                true
            }
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onPersonLoadSuccess(personModel: PersonModel) {
        binding.personData.visibility = View.VISIBLE

        binding.firstName.setText(personModel.firstName)
        binding.patronymic.setText(personModel.patronymic)
        binding.lastName.setText(personModel.lastName)
        binding.birthDate.showSoftInputOnFocus = false
        if (personModel.birthDate != null) {
            binding.birthDate.setText(dateFormat.format(personModel.birthDate))
        }
        binding.birthDate.setOnClickListener { showDatePicker() }
        binding.country.setText(personModel.country?.name)
        binding.height.setText(personModel.height.toString())
        binding.weight.setText(personModel.weight.toString())
        // load photo
        val photoImageUrl = PersonsPagingAdapter.personsImagesUrl + personModel.id + PersonsPagingAdapter.personsImagesExtension
        context?.let { it1 ->
            Glide
                .with(it1)
                .load(photoImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(binding.photo)
        }

        addTextChangeListeners()
    }

    private fun onPersonLoadFail() {
        binding.personData.visibility = View.GONE
        showToast("Something went wrong")
    }

    private fun addTextChangeListeners() {
        binding.firstName.doAfterTextChanged { text -> personViewModel.setFirstName(text.toString()) }
        binding.patronymic.doAfterTextChanged { text -> personViewModel.setPatronymic(text.toString()) }
        binding.lastName.doAfterTextChanged { text -> personViewModel.setLastName(text.toString()) }
        //binding.country.doAfterTextChanged { text -> personViewModel.setCountry(text.toString()) }
        binding.height.doAfterTextChanged { text -> personViewModel.setHeight(text.toString().toInt()) }
        binding.weight.doAfterTextChanged { text -> personViewModel.setWeight(text.toString().toInt()) }
    }

    private fun removeTextChangeListeners() {
    }

    private fun takePhoto() {
        TODO("Not yet implemented")
        /*val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 1)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }*/
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker() {
        // Use the current person birth date as the default date in the picker or today if null
        calendar.time = personViewModel.getBirthDate() ?: Date()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        binding.birthDate.setText(dateFormat.format(calendar.time))
        personViewModel.setBirthDate(calendar.time)
    }

}