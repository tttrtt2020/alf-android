/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.alf.ui.match.referees.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alf.ui.match.referees.RefereesPagingRepository

/**
 * Factory for ViewModels
 */
class RefereeSelectionViewModelFactory(
    private val repository: RefereesPagingRepository,
    private val matchId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefereeSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RefereeSelectionViewModel(repository, matchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
