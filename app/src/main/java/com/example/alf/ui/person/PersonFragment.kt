package com.example.alf.ui.person

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alf.R
import com.example.alf.databinding.FragmentPersonBinding
import com.example.alf.ui.persons.PersonsPagingAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PersonFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentPersonBinding

    private val args: PersonFragmentArgs by navArgs()

    private val personViewModel: PersonViewModel by viewModels()

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

        if (personViewModel.personLiveData?.value == null) {
            personViewModel.fetchPersonById(args.personId)
        }
        personViewModel.personLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                if (personViewModel.originalPersonLiveData == null) {
                    personViewModel.originalPersonLiveData?.value = it
                }

                binding.firstName.setText(it.firstName)
                binding.patronymic.setText(it.patronymic)
                binding.lastName.setText(it.lastName)
                binding.birthDate.showSoftInputOnFocus = false
                if (it.birthDate != null) {
                    binding.birthDate.setText(dateFormat.format(it.birthDate))
                }
                binding.birthDate.setOnClickListener { showDatePicker() }
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

                saveMenuItem.isEnabled = personViewModel.originalPersonLiveData?.value
                        ?.equals(personViewModel.personLiveData!!.value) ?: false

            } else {
                showToast("Something went wrong")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.person, menu)

        saveMenuItem = menu.findItem(R.id.action_save)

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
        // Use the current shown date as the default date in the picker
        val formattedDate = binding.birthDate.text.toString()
        try {
            val date = dateFormat.parse(formattedDate)
            if (date == null) {
                calendar.time = Date()
            } else {
                calendar.time = date
            }
        } catch (e : ParseException) {
            calendar.time = Date()
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        personViewModel.personLiveData?.value = personViewModel.personLiveData?.value
    }

}