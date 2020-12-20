package com.example.alf.ui.match.event

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alf.R
import com.example.alf.databinding.FragmentEventBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.MatchViewModelFactory
import com.google.android.material.snackbar.Snackbar

class EventFragment : Fragment() {

    companion object {
        fun newInstance() = EventFragment()

        const val MAX_MINUTE: Int = 90

        @JvmStatic
        @BindingAdapter("app:buttonImageUrl")
        fun loadTeamLogoImage(radioButton: RadioButton, url: String?) {

            if (!url.isNullOrEmpty()) {
                Glide
                        .with(radioButton.context)
                        .load(url)
                        .into(object : CustomTarget<Drawable?>() {
                            override fun onResourceReady(resource: Drawable, @Nullable transition: Transition<in Drawable?>?) {
                                radioButton.buttonDrawable = resource
                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        })
            }
        }
    }

    private lateinit var binding: FragmentEventBinding

    private val args: EventFragmentArgs by navArgs()

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory(args.matchId, args.eventId, args.event)
    }
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment) {
        MatchViewModelFactory(args.matchId)
    }

    private lateinit var saveMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.eventViewModel = eventViewModel
        binding.matchViewModel = matchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, {
            (activity as AppCompatActivity).supportActionBar?.title = it.eventType.name
        })

        eventViewModel.updateEventResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onUpdateEventResult(it)
                eventViewModel.updateEventResultLiveData.value = null
            }
        })

        eventViewModel.deleteEventResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onDeleteEventResult(it)
                eventViewModel.deleteEventResultLiveData.value = null
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.event, menu)

        saveMenuItem = menu.findItem(R.id.action_save)
        /*eventViewModel.saveEnabledLiveData.observe(viewLifecycleOwner, {
            saveMenuItem.isEnabled = it
        })*/

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    private fun onUpdateEventResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Update success") else showSnackBar(binding.root, "Update failed")
    }

    private fun onDeleteEventResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Delete success") else showSnackBar(binding.root, "Delete failed")
    }

    private fun save() {
        eventViewModel.saveEvent()
    }

    private fun delete() {
        eventViewModel.deleteEvent()
    }

    private fun showPersonPicker() {
        // Use the current person birth date as the default date in the picker or today if null
        /*calendar.time = personViewModel.getBirthDate() ?: Date()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        PersonPickerDialog(requireContext(), this, year, month, day).show()*/
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

}