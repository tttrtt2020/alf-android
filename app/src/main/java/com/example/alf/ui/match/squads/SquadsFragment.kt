package com.example.alf.ui.match.squads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alf.R
import com.example.alf.data.model.MatchModel
import com.example.alf.data.model.match.MatchPersonModel
import com.example.alf.databinding.FragmentMatchBinding
import com.example.alf.databinding.FragmentSquadsBinding
import com.example.alf.ui.match.MatchFragmentArgs
import com.example.alf.ui.match.MatchViewModel
import com.example.alf.ui.matches.MatchesPagingAdapter
import com.example.alf.ui.matches.MatchesViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SquadsFragment : Fragment(), MatchPersonsAdapter.SquadsListener {

    private lateinit var binding: FragmentSquadsBinding

    private lateinit var viewAdapter: MatchPersonsAdapter

    private val squadsViewModel by viewModels<SquadsViewModel>()

    //private val args: MatchFragmentArgs by navArgs()
    private val matchViewModel: MatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSquadsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val textView: TextView = view.findViewById(android.R.id.text1)
            textView.text = getInt(ARG_OBJECT).toString()
        }*/

        viewAdapter = MatchPersonsAdapter(this)
        binding.matchPersonsRecyclerView.apply {
            adapter = viewAdapter
        }

        //squadsViewModel.fetchMatchSquadsInfoById(args.matchId)
        //squadsViewModel.fetchMatchSquadsInfoById(matchViewModel.matchModelLiveData?.value?.id!!)
        /*squadsViewModel.matchPersonsModelLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(it.hostSquad.plus(it.guestSquad) as ArrayList<MatchPersonModel>)
            } else {
                showToast("Something went wrong")
            }
            binding.progressBar.visibility = View.GONE
        })*/
        /*matchViewModel.matchModelLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                squadsViewModel.fetchMatchSquadsInfoById(it)
                squadsViewModel.matchPersonsModelLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                        viewAdapter.setMatchPersons(it.hostSquad.plus(it.guestSquad) as ArrayList<MatchPersonModel>)
                    } else {
                        showToast("Something went wrong")
                    }
                    binding.progressBar.visibility = View.GONE
                })
                binding.progressBar.visibility = View.GONE
            } else {
                showToast("Something went wrong")
            }
        })*/

        matchViewModel.matchModelLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(getTeam() as ArrayList<MatchPersonModel>)

                binding.progressBar.visibility = View.GONE
            } else {
                showToast("Something went wrong")
            }
        })

        /*binding.matchPersonsRecyclerView.visibility = View.VISIBLE
        viewAdapter.setMatchPersons(matchViewModel.matchModelLiveData?.value?.squadsInfo?.hostSquad as ArrayList<MatchPersonModel>)
        binding.progressBar.visibility = View.GONE*/


        /*val tabLayout = binding.tabLayout2
        tabLayout.addTab(TabLayout.Tab())
        tabLayout.addTab(TabLayout.Tab())*/
        /*TabLayoutMediator(tabLayout, null) { tab, position ->
            tab.text = if (position == 0) "Home" else if (position == 1) "Events" else "Away"
        }.attach()*/

    }

    private fun getTeam(): List<MatchPersonModel>? {
        return when {
            requireArguments().get("team") == "host" -> matchViewModel.matchModelLiveData!!.value!!.squadsInfo.hostSquad
            requireArguments().get("team") == "guest" -> matchViewModel.matchModelLiveData!!.value!!.squadsInfo.guestSquad
            else -> null
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPersonModel: MatchPersonModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchPersonModel: MatchPersonModel, position: Int) {
        TODO("Not yet implemented")
    }
}