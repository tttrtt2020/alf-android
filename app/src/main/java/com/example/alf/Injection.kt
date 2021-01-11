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

package com.example.alf

import androidx.lifecycle.ViewModelProvider
import com.example.alf.service.MatchesService
import com.example.alf.service.PersonsService
import com.example.alf.service.PlayersService
import com.example.alf.service.RefereesService
import com.example.alf.ui.match.players.PlayersPagingRepository
import com.example.alf.ui.match.players.selection.PlayerSelectionFragmentArgs
import com.example.alf.ui.match.players.selection.PlayerSelectionViewModelFactory
import com.example.alf.ui.matches.MatchesPagingRepository
import com.example.alf.ui.matches.MatchesViewModelFactory
import com.example.alf.ui.persons.PersonsPagingRepository
import com.example.alf.ui.persons.PersonsViewModelFactory
import com.example.alf.ui.match.referees.RefereesPagingRepository
import com.example.alf.ui.match.referees.selection.RefereeSelectionViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [PersonsRepository] based on the [PersonsService] and a
     * [PersonsLocalCache]
     */
    private fun providePersonsRepository(): PersonsPagingRepository {
        return PersonsPagingRepository(PersonsService())
    }

    /**
     * Creates an instance of [PersonsRepository] based on the [PersonsService] and a
     * [PersonsLocalCache]
     */
    private fun provideMatchesRepository(): MatchesPagingRepository {
        return MatchesPagingRepository(MatchesService())
    }

    /**
     * Creates an instance of [PlayersRepository] based on the [PlayersService] and a
     * [PlayersLocalCache]
     */
    private fun providePlayersRepository(): PlayersPagingRepository {
        return PlayersPagingRepository(PlayersService())
    }

    /**
     * Creates an instance of [RefereesRepository] based on the [RefereesService] and a
     * [RefereesLocalCache]
     */
    private fun provideRefereesRepository(): RefereesPagingRepository {
        return RefereesPagingRepository(RefereesService())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun providePersonsViewModelFactory(): ViewModelProvider.Factory {
        return PersonsViewModelFactory(providePersonsRepository())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideMatchesViewModelFactory(): ViewModelProvider.Factory {
        return MatchesViewModelFactory(provideMatchesRepository())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideRefereesViewModelFactory(matchId: Int): ViewModelProvider.Factory {
        return RefereeSelectionViewModelFactory(provideRefereesRepository(), matchId)
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun providePlayersViewModelFactory(args: PlayerSelectionFragmentArgs): ViewModelProvider.Factory {
        return PlayerSelectionViewModelFactory(providePlayersRepository(), args)
    }
}
