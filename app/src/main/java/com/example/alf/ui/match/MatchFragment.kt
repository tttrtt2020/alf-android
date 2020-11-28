package com.example.alf.ui.match

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
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
import com.google.android.material.tabs.TabLayoutMediator


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
        MatchViewModelFactory(activity?.application!!, args.matchId)
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

        matchViewModel.getMatchInfoResultLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                onGetMatchResult(it)
                matchViewModel.getMatchInfoResultLiveData.value = null
            }
        })

        binding.pager.adapter = MatchInfoAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_name_host_squad)
                2 -> getString(R.string.tab_name_guest_squad)
                1 -> getString(R.string.tab_name_events)
                else -> null
            }
        }.attach()

        binding.fab.setOnClickListener { onFabClicked() }
    }

    private fun onGetMatchResult(success: Boolean) {
        if (success) showSnackBar(binding.root, "Get success") else showSnackBar(binding.root, "Get failed")
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun onFabClicked() {
        val action = MatchFragmentDirections.actionMatchFragmentToLiveEventTypesFragment(args.matchId)
        findNavController().navigate(action)
    }

}