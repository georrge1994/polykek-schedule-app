package com.android.feature.buildings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.map.BUILDING_ITEM
import com.android.common.models.map.Building
import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.feature.buildings.R
import com.android.feature.buildings.adapters.BuildingRecyclerViewAdapter
import com.android.feature.buildings.adapters.IBuildingActions
import com.android.feature.buildings.dagger.BuildingsComponentHolder
import com.android.feature.buildings.databinding.FragmentBuildingsBinding
import com.android.feature.buildings.mvi.BuildingAction
import com.android.feature.buildings.mvi.BuildingIntent
import com.android.feature.buildings.mvi.BuildingState
import com.android.feature.buildings.viewModels.BuildingViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent

/**
 * List of all Polytech's buildings.
 *
 * @constructor Create empty constructor for building list fragment
 */
internal class BuildingsFragment : SearchToolbarFragment<BuildingIntent, BuildingState, BuildingAction, BuildingViewModel>(
    BuildingViewModel::class
) {
    private val viewBinding by viewBinding(FragmentBuildingsBinding::bind)
    private lateinit var adapter: BuildingRecyclerViewAdapter

    private val clickListener = object : IBuildingActions {
        override fun onClick(building: Building) {
            BuildingIntent.BuildingSelected(building).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = BuildingsComponentHolder.getComponent()

    override fun injectToComponent() = BuildingsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BuildingIntent.LoadContent.dispatchIntent()
        adapter = BuildingRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_buildings, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.map_buildings_list_of_building, true)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun getSearchIntent(keyWord: String?): BuildingIntent = BuildingIntent.KeyWordChanged(keyWord)

    override fun invalidateUi(state: BuildingState) {
        super.invalidateUi(state)
        viewBinding.animation.root.isVisible = state.isLoading
        viewBinding.listIsEmpty.isVisible = state.isLoading.not() && state.buildings.isEmpty()
        adapter.updateItems(state.buildings)
    }

    override fun executeSingleAction(action: BuildingAction) {
        super.executeSingleAction(action)
        if (action is BuildingAction.SelectBuilding) {
            showBuilding(action.building)
        }
    }

    /**
     * Show building.
     *
     * @param building Building
     */
    private fun showBuilding(building: Building) {
        setFragmentResult(BUILDING_ITEM, bundleOf(BUILDING_ITEM to building))
        tabRouter?.exit()
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}