package com.android.feature.buildings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.map.BUILDING_ITEM
import com.android.common.models.map.Building
import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.feature.buildings.R
import com.android.feature.buildings.adapters.BuildingRecyclerViewAdapter
import com.android.feature.buildings.adapters.IBuildingActions
import com.android.feature.buildings.dagger.BuildingsComponentHolder
import com.android.feature.buildings.databinding.FragmentBuildingsBinding
import com.android.feature.buildings.viewModels.BuildingViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent

/**
 * List of all Polytech's buildings.
 *
 * @constructor Create empty constructor for building list fragment
 */
internal class BuildingsFragment : SearchToolbarFragment<BuildingViewModel>(BuildingViewModel::class) {
    private val viewBinding by viewBinding(FragmentBuildingsBinding::bind)
    private lateinit var adapter: BuildingRecyclerViewAdapter

    private val clickListener = object : IBuildingActions {
        override fun onClick(building: Building) {
            showBuilding(building)
        }
    }

    private val buildingsObserver = Observer<List<Building>?> { adapter.updateItems(it) }

    private val isListEmptyObserver = Observer<Boolean> { viewBinding.listIsEmpty.isVisible = it }

    private val isLoadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun getComponent(): IModuleComponent = BuildingsComponentHolder.getComponent()

    override fun injectToComponent() = BuildingsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateBuildings()
        adapter = BuildingRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_buildings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.map_buildings_list_of_building, true)
        viewBinding.recyclerView.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, isLoadingObserver)
        viewModel.buildings.observe(viewLifecycleOwner, buildingsObserver)
        viewModel.isListEmpty.observe(viewLifecycleOwner, isListEmptyObserver)
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