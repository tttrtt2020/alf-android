package com.example.alf.ui.match

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
import com.example.alf.ui.matches.MatchesAdapter

class MatchFragment : Fragment() {

    private lateinit var matchViewModel: MatchViewModel

    private lateinit var hostLogoImageView: ImageView
    private lateinit var guestLogoImageView: ImageView
    private lateinit var hostNameTextView: TextView
    private lateinit var guestNameTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView

    private val args: MatchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        matchViewModel = ViewModelProvider(this).get(MatchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_match, container, false)

        hostLogoImageView = root.findViewById(R.id.host_logo)
        guestLogoImageView = root.findViewById(R.id.guest_logo)
        hostNameTextView = root.findViewById(R.id.host_name)
        guestNameTextView = root.findViewById(R.id.guest_name)
        resultTextView = root.findViewById(R.id.result)
        dateTextView = root.findViewById(R.id.date)
        timeTextView = root.findViewById(R.id.time)

        matchViewModel = ViewModelProvider(this)[MatchViewModel::class.java]
        matchViewModel.fetchMatchById(args.matchId)
        matchViewModel.matchModelLiveData?.observe(viewLifecycleOwner, {
            if (it != null) {
                hostNameTextView.text = it.hostMatchTeam.team.name
                guestNameTextView.text = it.guestMatchTeam.team.name
                resultTextView.text = it.status
                /*if (it.dateTime != null) {
                    dateDatePicker.updateDate(it.dateTime!!.year, it.dateTime?.month, it.dateTime?.day)
                }*/
                // load club logos
                val hostClubLogoUrl = MatchesAdapter.clubLogosUrl + it.hostMatchTeam.team.club.id + MatchesAdapter.clubLogosExtension
                context?.let { it1 ->
                    Glide
                        .with(it1)
                        .load(hostClubLogoUrl)
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.holo_red_dark)
                        .into(hostLogoImageView)
                }
                val guestClubLogoUrl = MatchesAdapter.clubLogosUrl + it.guestMatchTeam.team.club.id + MatchesAdapter.clubLogosExtension
                context?.let { it1 ->
                    Glide
                        .with(it1)
                        .load(guestClubLogoUrl)
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.holo_red_dark)
                        .into(guestLogoImageView)
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