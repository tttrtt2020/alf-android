package com.example.alf.ui.match.event.selection.minute

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alf.R
import com.example.alf.databinding.FragmentMinuteSelectionBinding
import com.google.android.material.snackbar.Snackbar


class MinuteSelectionFragment : Fragment() {

    private lateinit var binding: FragmentMinuteSelectionBinding

    private val args: MinuteSelectionFragmentArgs by navArgs()

    private lateinit var saveMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMinuteSelectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.minute.requestFocus()
        binding.minute.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                onProceedClicked()
                true
            } else false
        }
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.minute_selection, menu)

        saveMenuItem = menu.findItem(R.id.action_proceed)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_proceed -> {
                onProceedClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onProceedClicked() {
        if (validateMinute()) {
            proceed()
        } else showSnackBar(binding.root, "Please set correct minute")
    }

    private fun validateMinute(): Boolean {
        val text = binding.minute.text.toString()
        return try {
            val minute = Integer.parseInt(text)
            minute >= 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun getMinute(): Int {
        val text = binding.minute.text.toString()
        return Integer.parseInt(text)
    }

    private fun proceed() {
        val action = MinuteSelectionFragmentDirections.actionMinuteSelectionFragmentToTeamSelectionFragment(
                matchId = args.matchId,
                hostTeamId = args.hostTeamId, guestTeamId = args.guestTeamId,
                minute = getMinute(),
                mode = args.mode
        )
        findNavController().navigate(action)
    }

    private fun showKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.minute, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // passing 0 closes keyboard even if it is shown explicitly by the user, not only using
        // showSoftInput() method with InputMethodManager.SHOW_IMPLICIT flag
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

}