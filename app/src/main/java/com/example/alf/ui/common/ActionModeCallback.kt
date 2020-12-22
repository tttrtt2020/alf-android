package com.example.alf.ui.common

import android.content.Context
import android.view.Menu
import android.view.View
import androidx.appcompat.view.ActionMode
import com.example.alf.R

abstract class ActionModeCallback(
        context: Context,
        private val view: View
) : ActionMode.Callback {

    private val selectedStateColor = context.resources
            .getColor(R.color.item_selected_color, context.theme)
    private val unselectedStateColor = context.resources
            .getColor(R.color.item_color, context.theme)

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        view.setBackgroundColor(selectedStateColor)
        return false // Return false if nothing is done
    }

    // Called when the user exits the action mode
    override fun onDestroyActionMode(mode: ActionMode) {
        view.setBackgroundColor(unselectedStateColor)
    }
}