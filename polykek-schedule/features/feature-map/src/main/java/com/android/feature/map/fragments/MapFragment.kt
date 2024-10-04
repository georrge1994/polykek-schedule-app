package com.android.feature.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.map.BUILDING_ITEM
import com.android.common.models.map.Building.Companion.getBuilding
import com.android.core.ui.mvi.MviFragment
import com.android.feature.map.R
import com.android.feature.map.dagger.MapComponentHolder
import com.android.feature.map.databinding.FragmentMapBinding
import com.android.feature.map.mvi.MapAction
import com.android.feature.map.mvi.MapIntent
import com.android.feature.map.mvi.MapState
import com.android.feature.map.repositories.MapKitInitializer
import com.android.feature.map.useCases.MapActionsUiUseCase
import com.android.feature.map.viewModels.MapViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObjectTapListener
import javax.inject.Inject

/**
 * The map fragment is a third tab on the main screen.
 *
 * @constructor Create empty constructor for map fragment
 */
internal class MapFragment : MviFragment<MapIntent, MapState, MapAction, MapViewModel>() {
    private val viewBinding by viewBinding(FragmentMapBinding::bind)
    private var titles: Array<String>? = null

    @Inject
    lateinit var mapActionsUiUseCase: MapActionsUiUseCase

    // Listeners.
    private val circleMapObjectTapListener = MapObjectTapListener { mapObject, _ ->
        MapIntent.SelectMapObject(mapObject.userData).dispatchIntent()
        true
    }

    private val cameraListener = CameraListener { _, _, cameraUpdateReason, _ ->
        if (cameraUpdateReason != CameraUpdateReason.APPLICATION)
            MapIntent.DeselectMapObject.dispatchIntent()
    }

    private val nextDayListener = View.OnClickListener { MapIntent.ShowNextDay.dispatchIntent() }

    private val previousDayListener = View.OnClickListener { MapIntent.ShowPreviousDay.dispatchIntent() }

    // Observers.
    override fun getComponent(): IModuleComponent = MapComponentHolder.getComponent()

    override fun injectToComponent() = MapComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitInitializer.initialize(requireContext())
        viewModel = createViewModel(viewModelFactory)
        titles = context?.resources?.getStringArray(R.array.schedule_fragment_day_abbreviations)
        setFragmentResultListener(BUILDING_ITEM) { _, bundle ->
            MapIntent.BuildingSearchResult(bundle.getBuilding()).dispatchIntent()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewModel.asyncSubscribe()
        viewBinding.mapView.mapWindow.map.addCameraListener(cameraListener)
        viewBinding.nextBtn.setOnClickListener(nextDayListener)
        viewBinding.previousBtn.setOnClickListener(previousDayListener)
    }

    override fun invalidateUi(state: MapState) {
        super.invalidateUi(state)
        // Remove previous map objects and add new.
        viewBinding.mapView.mapWindow.map.mapObjects.clear()
        state.yandexMapItems.forEach {
            mapActionsUiUseCase.addPlaceMark(context, viewBinding.mapView.mapWindow.map, it, circleMapObjectTapListener)
        }
        state.searchResultMapItem?.let { yandexMapItem ->
            mapActionsUiUseCase.addPlaceMark(
                context,
                viewBinding.mapView.mapWindow.map,
                yandexMapItem,
                circleMapObjectTapListener
            )
        }
        // Update control buttons` state.
        viewBinding.previousBtn.isEnabled = state.dayControls?.isPreviousEnabled ?: true
        viewBinding.nextBtn.isEnabled = state.dayControls?.isNextEnabled ?: true
        viewBinding.dayAbbr.text = titles?.get(state.dayControls?.dayIndex ?: 0)
    }

    override fun executeSingleAction(action: MapAction) {
        super.executeSingleAction(action)
        with(viewBinding.mapView.mapWindow.map) {
            when (action) {
                is MapAction.DefaultFocus -> mapActionsUiUseCase.move(this, action.boundingBox)
                is MapAction.ShowBuildingSearchOnTheMap -> mapActionsUiUseCase.move(this, action.point)
                is MapAction.UpdateCameraPosition -> mapActionsUiUseCase.move(this, action.cameraPosition)
                else -> { /* Nothing doing. */ }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewBinding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDestroyView() {
        // Save camera position.
        MapIntent.SaveCameraPosition(viewBinding.mapView.mapWindow.map.cameraPosition).dispatchIntent()
        viewBinding.mapView.mapWindow.map.removeCameraListener(cameraListener)
        super.onDestroyView()
        viewModel.unSubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(BUILDING_ITEM)
    }
}