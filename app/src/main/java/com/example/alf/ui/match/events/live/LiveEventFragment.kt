package com.example.alf.ui.match.events.live

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
import com.example.alf.databinding.FragmentLiveEventBinding
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.match.MatchViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LiveEventFragment : Fragment() {

    companion object {
        fun newInstance() = LiveEventFragment()

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

    private lateinit var binding: FragmentLiveEventBinding

    private val args: LiveEventFragmentArgs by navArgs()

    private val liveEventViewModel: LiveEventViewModel by viewModels {
        LiveEventViewModelFactory(args.liveEventTypeId, args.liveEventType)
    }
    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment) {
        MatchViewModelFactory(activity?.application!!, args.matchId)
    }

    private lateinit var saveMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLiveEventBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.liveEventViewModel = liveEventViewModel
        binding.matchViewModel = matchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        liveEventViewModel.nameLiveData.observe(viewLifecycleOwner, {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.live_event, menu)

        saveMenuItem = menu.findItem(R.id.action_save)
        liveEventViewModel.saveEnabledLiveData.observe(viewLifecycleOwner, {
            saveMenuItem.isEnabled = it
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        liveEventViewModel.saveLiveEvent()
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

}