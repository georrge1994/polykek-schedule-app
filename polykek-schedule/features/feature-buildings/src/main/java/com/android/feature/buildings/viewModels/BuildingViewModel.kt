package com.android.feature.buildings.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.common.models.map.Building
import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.buildings.useCases.BuildingUseCase
import javax.inject.Inject

/**
 * Building view model.
 *
 * @property buildingUseCase Provides list of buildings fetched from Polytech server and sorted by keyword
 * @constructor Create [BuildingViewModel]
 */
internal class BuildingViewModel @Inject constructor(private val buildingUseCase: BuildingUseCase) : SearchViewModel() {
    val buildings = MutableLiveData<List<Building>>()
    val isListEmpty = Transformations.map(buildings) { it.isEmpty() }

    override suspend fun keyWordWasChanged(keyWord: String?) {
        super.keyWordWasChanged(keyWord)
        updateBuildings(keyWord)
    }

    /**
     * Update buildings.
     *
     * @param keyWord Key word
     */
    internal fun updateBuildings(keyWord: String? = "") = executeWithLoadingAnimation {
        buildingUseCase.getBuildings(keyWord).let { filteredBuildings ->
            buildings.postValue(filteredBuildings)
        }
    }
}