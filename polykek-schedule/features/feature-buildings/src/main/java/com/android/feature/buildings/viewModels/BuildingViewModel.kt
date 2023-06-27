package com.android.feature.buildings.viewModels

import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.buildings.mvi.BuildingAction
import com.android.feature.buildings.mvi.BuildingIntent
import com.android.feature.buildings.mvi.BuildingState
import com.android.feature.buildings.useCases.BuildingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Building view model.
 *
 * @property buildingUseCase Provides list of buildings fetched from Polytech server and sorted by keyword
 * @constructor Create [BuildingViewModel]
 */
internal class BuildingViewModel @Inject constructor(
    private val buildingUseCase: BuildingUseCase
) : SearchViewModel<BuildingIntent, BuildingState, BuildingAction>(BuildingState.Default) {
    override suspend fun dispatchIntent(intent: BuildingIntent) {
        when (intent) {
            is BuildingIntent.LoadContent -> updateBuildings(keyWordFromLastState)
            is BuildingIntent.KeyWordChanged -> updateBuildings(intent.keyWord)
            is BuildingIntent.BuildingSelected -> BuildingAction.SelectBuilding(intent.building).emitAction()
        }
    }

    /**
     * Update buildings.
     *
     * @param keyWord Key word
     */
    private suspend fun updateBuildings(keyWord: String?) = withContext(Dispatchers.IO) {
        currentState.copyState(keyWord = keyWord, isLoading = true).emitState()
        currentState.copyState(buildings = buildingUseCase.getBuildings(keyWord), isLoading = false).emitState()
    }
}