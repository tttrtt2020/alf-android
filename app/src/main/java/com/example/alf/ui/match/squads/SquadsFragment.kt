package com.example.alf.ui.match.squads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.databinding.FragmentSquadsBinding
import com.example.alf.ui.match.MatchFragmentDirections
import com.example.alf.ui.match.MatchViewModel


class SquadsFragment : Fragment(), MatchPersonsAdapter.SquadsListener, View.OnClickListener {

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

        binding.formation.setOnClickListener(this)

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
        //squadsViewModel.fetchMatchSquadsInfoById(matchViewModel.matchLiveData?.value?.id!!)
        /*squadsViewModel.matchPersonsModelLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(it.hostSquad.plus(it.guestSquad) as ArrayList<MatchPerson>)
            } else {
                showToast("Something went wrong")
            }
            binding.progressBar.visibility = View.GONE
        })*/
        /*matchViewModel.matchLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                squadsViewModel.fetchMatchSquadsInfoById(it)
                squadsViewModel.matchPersonsModelLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                        viewAdapter.setMatchPersons(it.hostSquad.plus(it.guestSquad) as ArrayList<MatchPerson>)
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

        matchViewModel.matchInfoLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.matchPersonsRecyclerView.visibility = View.VISIBLE
                viewAdapter.setMatchPersons(getTeam() as ArrayList<MatchPerson>)

                binding.progressBar.visibility = View.GONE
            } else {
                showToast("Something went wrong")
            }
        })

        /*binding.matchPersonsRecyclerView.visibility = View.VISIBLE
        viewAdapter.setMatchPersons(matchViewModel.matchLiveData?.value?.squadsInfo?.hostSquad as ArrayList<MatchPerson>)
        binding.progressBar.visibility = View.GONE*/


        /*val tabLayout = binding.tabLayout2
        tabLayout.addTab(TabLayout.Tab())
        tabLayout.addTab(TabLayout.Tab())*/
        /*TabLayoutMediator(tabLayout, null) { tab, position ->
            tab.text = if (position == 0) "Home" else if (position == 1) "Events" else "Away"
        }.attach()*/

    }

    private fun getTeam(): List<MatchPerson>? {
        return when {
            requireArguments().get("team") == "host" -> matchViewModel.hostSquadLiveData.value
            requireArguments().get("team") == "guest" -> matchViewModel.guestSquadLiveData.value
            else -> null
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onItemDeleted(matchPerson: MatchPerson, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(matchPerson: MatchPerson, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        val matchId = matchViewModel.matchLiveData.value?.mainInfo?.match?.id
        val action = matchId.let { MatchFragmentDirections.actionMatchFragmentToFormationsFragment() }
        findNavController().navigate(action)
    }
}