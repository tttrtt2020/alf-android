package com.example.alf.ui.match

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.alf.AlfApplication
import com.example.alf.databinding.FragmentMatchBinding
import com.example.alf.ui.matches.MatchesAdapter
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import java.text.SimpleDateFormat
import java.util.*


class MatchFragment : Fragment() {

    private lateinit var binding: FragmentMatchBinding

    private lateinit var matchInfoAdapter: MatchInfoAdapter
    private lateinit var viewPager: ViewPager2


    //private lateinit var matchViewModel: MatchViewModel
    private val matchViewModel: MatchViewModel by activityViewModels()

    private val args: MatchFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchViewModel.getMatchById(args.matchId)
        matchViewModel.matchLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.hostName.text = it.mainInfo.match.hostTeam.name
                binding.guestName.text = it.mainInfo.match.guestTeam.name
                binding.result.text = it.mainInfo.match.status
                /*if (it.dateTime != null) {
                    dateDatePicker.updateDate(it.dateTime!!.year, it.dateTime?.month, it.dateTime?.day)
                }*/

                binding.result.text = if (it.mainInfo.match.status == "FINISHED")
                    (it.mainInfo.match.result.hostGoals.toString() + ":" + it.mainInfo.match.result.guestGoals.toString())
                else "- : -"
                binding.date.text = if (it.mainInfo.match.dateTime == null) "-" else
                    SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(it.mainInfo.match.dateTime)
                binding.time.text = if (it.mainInfo.match.dateTime == null) "-" else
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.mainInfo.match.dateTime)

                // load stadium background
                val stadiumBackgroundUrl = AlfApplication.getProperty("url.image.stadium.background") + (it.mainInfo.match.stadium?.id
                        ?: "fallback") + AlfApplication.getProperty("extension.stadium.background")
                val multi = MultiTransformation<Bitmap>(
                        BlurTransformation(10),
                        GrayscaleTransformation(),
                )
                Glide.with(this).load(stadiumBackgroundUrl)
                        //.apply(bitmapTransform(multi))
                        .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, @Nullable transition: Transition<in Drawable?>?) {
                        binding.mainInfoFrameLayout.background = resource
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                })

                // load club logos
                val hostClubLogoUrl = MatchesAdapter.clubLogosUrl + it.mainInfo.match.hostTeam.clubId + MatchesAdapter.clubLogosExtension
                context?.let { it1 ->
                    Glide
                            .with(it1)
                            .load(hostClubLogoUrl)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.holo_red_dark)
                            .into(binding.hostLogo)
                }
                val guestClubLogoUrl = MatchesAdapter.clubLogosUrl + it.mainInfo.match.guestTeam.clubId + MatchesAdapter.clubLogosExtension
                context?.let { it1 ->
                    Glide
                            .with(it1)
                            .load(guestClubLogoUrl)
                            .placeholder(android.R.color.darker_gray)
                            .error(android.R.color.holo_red_dark)
                            .into(binding.guestLogo)
                }
            } else {
                showToast("Something went wrong")
            }
        })

        matchInfoAdapter = MatchInfoAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = matchInfoAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Host team" else if (position == 1) "Events" else "Guest team"
        }.attach()
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}