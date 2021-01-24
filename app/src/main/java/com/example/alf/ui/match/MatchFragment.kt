package com.example.alf.ui.match

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alf.R
import com.example.alf.databinding.FragmentMatchBinding
import com.google.android.material.snackbar.Snackbar


class MatchFragment : Fragment() {

    companion object {
        @JvmStatic
        @BindingAdapter("app:imageUrl")
        fun loadTeamLogo(imageView: ImageView, url: String?) {

            if (!url.isNullOrEmpty()) {
                Glide
                    .with(imageView.context)
                    .load(url)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:backgroundImageUrl")
        fun loadStadiumBackgroundImage(view: View, url: String?) {

            if (!url.isNullOrEmpty()) {
                Glide
                    .with(view.context)
                    .load(url)
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onResourceReady(resource: Drawable, @Nullable transition: Transition<in Drawable?>?) {
                            view.background = resource
                        }

                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                    })
            }
        }
    }

    private lateinit var binding: FragmentMatchBinding

    private val args: MatchFragmentArgs by navArgs()

    private val matchViewModel: MatchViewModel by navGraphViewModels(R.id.matchFragment) {
        MatchViewModelFactory(args.matchId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.matchViewModel = matchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeMatchViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_youtube -> {
                // TODO: 1/24/21 openYoutube()
                true
            }
            R.id.action_open_referees -> {
                openReferees()
                true
            }
            R.id.action_open_events -> {
                openEvents()
                true
            }
            R.id.action_open_substitutions -> {
                // TODO: 1/24/21 openSubstitutions()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        binding.hostLayout.setOnClickListener { onHostClicked() }
        binding.guestLayout.setOnClickListener { onGuestClicked() }

        binding.bottomNavigation.apply {
            itemIconTintList = null
            setOnNavigationItemSelectedListener { item -> onOptionsItemSelected(item) }
        }
    }

    private fun observeMatchViewModel() {
        matchViewModel.getMatchResultLiveData.observe(viewLifecycleOwner, {
            onGetMatchResult(it)
        })
    }

    private fun onGetMatchResult(success: Boolean?) {
        if (success != null) {
            if (success) {
                matchViewModel.getMatchResultLiveData.value = null
            } else showSnackBar(binding.root, "Get failed")
        }
    }

    private fun openReferees() {
        val action = MatchFragmentDirections.actionMatchFragmentToMatchRefereesFragment(args.matchId)
        findNavController().navigate(action)
    }

    private fun openEvents() {
        matchViewModel.matchLiveData.value?.let {
            val action = MatchFragmentDirections.actionMatchFragmentToEventsFragment(
                    args.matchId,
                    it.data!!.hostTeam.id,
                    it.data.guestTeam.id
            )
            findNavController().navigate(action)
        }
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun onHostClicked() {
        matchViewModel.hostTeamLiveData.value?.let {
            val action = MatchFragmentDirections.actionMatchFragmentToTeamFragment(
                    args.matchId,
                    it.id
            )
            findNavController().navigate(action)
        }
    }

    private fun onGuestClicked() {
        matchViewModel.guestTeamLiveData.value?.let {
            val action = MatchFragmentDirections.actionMatchFragmentToTeamFragment(
                    args.matchId,
                    it.id
            )
            findNavController().navigate(action)
        }
    }

}