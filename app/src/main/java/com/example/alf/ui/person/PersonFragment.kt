package com.example.alf.ui.person

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.alf.AlfApplication
import com.example.alf.R
import com.example.alf.databinding.FragmentPersonBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class PersonFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        @JvmStatic
        @BindingAdapter("app:imageUrl")
        fun loadImage(imageView: ImageView, url: String?) {

            if (!url.isNullOrEmpty()) {
                Glide
                    .with(imageView.context)
                    .load(url)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(imageView)
            }
        }
    }

    private lateinit var binding: FragmentPersonBinding

    private val args: PersonFragmentArgs by navArgs()

    private val personViewModel: PersonViewModel by viewModels { PersonViewModelFactory(activity?.application!!, args.personId) }

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat(AlfApplication.getProperty("dateFormat"), Locale.getDefault())

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
        binding = FragmentPersonBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.personViewModel = personViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personViewModel.getPersonResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onGetPersonResult(it)
                personViewModel.getPersonResultLiveData.value = null
            }
        })

        personViewModel.createPersonResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onCreatePersonResult(it)
                personViewModel.createPersonResultLiveData.value = null
            }
        })

        personViewModel.updatePersonResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onUpdatePersonResult(it)
                personViewModel.updatePersonResultLiveData.value = null
            }
        })

        personViewModel.deletePersonResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onDeletePersonResult(it)
                personViewModel.deletePersonResultLiveData.value = null
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
            R.id.action_delete -> {
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onGetPersonResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Get success") else showSnackBar(binding.root, "Get failed")
    }

    private fun onCreatePersonResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Create success") else showSnackBar(binding.root, "Create failed")
    }

    private fun onUpdatePersonResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Update success") else showSnackBar(binding.root, "Update failed")
    }

    private fun onDeletePersonResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Delete success") else showSnackBar(binding.root, "Delete failed")
    }

    private fun takePhoto() {
        TODO("Not yet implemented")
        /*val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, 1)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }*/ }

    private fun save() {
        personViewModel.savePerson()
    }

    private fun delete() {
        personViewModel.deletePerson()
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
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