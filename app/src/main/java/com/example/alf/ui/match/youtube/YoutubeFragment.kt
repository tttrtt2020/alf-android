package com.example.alf.ui.match.youtube

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alf.R
import com.example.alf.databinding.FragmentYoutubeBinding
import com.google.android.material.snackbar.Snackbar


class YoutubeFragment : Fragment() {

    private lateinit var binding: FragmentYoutubeBinding

    private val args: YoutubeFragmentArgs by navArgs()

    private lateinit var doneMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentYoutubeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.videoUrl.requestFocus()
        binding.videoUrl.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                onDoneClicked()
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
        inflater.inflate(R.menu.youtube, menu)
        doneMenuItem = menu.findItem(R.id.action_done)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_proceed -> {
                onDoneClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onDoneClicked() {
        if (validateVideoUrl()) {
            // TODO: 1/24/21 implement set video URL logic
            //done()
        } else showSnackBar(binding.root, "Please set correct URL")
    }

    private fun validateVideoUrl(): Boolean {
        // TODO: 1/24/21 add validation
        val text = binding.videoUrl.text.toString().trim()
        return true
    }

    private fun getVideoUrl(): String {
        return binding.videoUrl.text.toString().trim()
    }

    private fun done() {

    }

    private fun showKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.videoUrl, InputMethodManager.SHOW_IMPLICIT)
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