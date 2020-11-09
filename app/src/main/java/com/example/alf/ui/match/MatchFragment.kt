package com.example.alf.ui.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.alf.databinding.FragmentMatchBinding
import com.example.alf.ui.matches.MatchesAdapter
import com.google.android.material.tabs.TabLayoutMediator

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

        //matchViewModel = ViewModelProvider(this)[MatchViewModel::class.java]
        matchViewModel.fetchMatchById(args.matchId)
        matchViewModel.matchModelLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.hostName.text = it.mainInfo.match.hostTeam.name
                binding.guestName.text = it.mainInfo.match.guestTeam.name
                binding.result.text = it.mainInfo.match.status
                /*if (it.dateTime != null) {
                    dateDatePicker.updateDate(it.dateTime!!.year, it.dateTime?.month, it.dateTime?.day)
                }*/
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