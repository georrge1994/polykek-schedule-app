package com.android.feature.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.map.BUILDING_ITEM
import com.android.common.models.map.Building.Companion.getBuilding
import com.android.core.ui.fragments.BaseFragment
import com.android.feature.map.R
import com.android.feature.map.dagger.MapComponentHolder
import com.android.feature.map.databinding.FragmentMapBinding
import com.android.feature.map.models.DayControls
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.repositories.MapKitInitializer
import com.android.feature.map.useCases.MapActionsUiUseCase
import com.android.feature.map.viewModels.MapViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObjectTapListener
import javax.inject.Inject

/**
 * The map fragment is a third tab on the main screen.
 *
 * @constructor Create empty constructor for map fragment
 */
internal class MapFragment : BaseFragment() {
    private val viewBinding by viewBinding(FragmentMapBinding::bind)
    private lateinit var mapViewModel: MapViewModel
    private var titles: Array<String>? = null

    @Inject
    lateinit var mapActionsUiUseCase: MapActionsUiUseCase

    // Listeners.
    private val circleMapObjectTapListener = MapObjectTapListener { mapObject, _ ->
        mapViewModel.selectMapObject(mapObject.userData)
        true
    }

    private val cameraListener = CameraListener { _, _, cameraUpdateReason, _ ->
        if (cameraUpdateReason != CameraUpdateReason.APPLICATION)
            mapViewModel.deselectMapObject()
    }

    private val nextDayListener = View.OnClickListener {
        viewBinding.mapView.map.mapObjects.clear()
        mapViewModel.showNextDay()
    }

    private val previousDayListener = View.OnClickListener {
        viewBinding.mapView.map.mapObjects.clear()
        mapViewModel.showPreviousDay()
    }

    // Observers.
    private val boundingBoxObserver = Observer<BoundingBox> { mapActionsUiUseCase.move(viewBinding.mapView.map, it) }

    private val dayControlsStateObserver = Observer<DayControls> {
        viewBinding.previousBtn.isEnabled = it.isPreviousEnabled
        viewBinding.nextBtn.isEnabled = it.isNextEnabled
        viewBinding.dayAbbr.text = titles?.get(it.dayIndex)
    }

    private val yandexMapItemsObserver = Observer<List<YandexMapItem>> { items ->
        items.forEach {
            mapActionsUiUseCase.addPlaceMark(context, viewBinding.mapView.map, it, circleMapObjectTapListener)
        }
    }

    override fun getComponent(): IModuleComponent = MapComponentHolder.getComponent()

    override fun injectToComponent() = MapComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitInitializer.initialize(requireContext())
        mapViewModel = createViewModel(viewModelFactory)
        titles = context?.resources?.getStringArray(R.array.schedule_fragment_day_abbreviations)
        setFragmentResultListener(BUILDING_ITEM) { _, bundle ->
            mapViewModel.getYandexMapItem(bundle.getBuilding())?.let { yandexMapItem ->
                mapActionsUiUseCase.addPlaceMark(context, viewBinding.mapView.map, yandexMapItem, circleMapObjectTapListener)
                mapActionsUiUseCase.move(viewBinding.mapView.map, yandexMapItem.point)
                mapViewModel.selectMapObject(yandexMapItem.userData)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.asyncSubscribe()

        viewBinding.mapView.map.addCameraListener(cameraListener)
        viewBinding.nextBtn.setOnClickListener(nextDayListener)
        viewBinding.previousBtn.setOnClickListener(previousDayListener)

        mapViewModel.yandexMapItems.observe(viewLifecycleOwner, yandexMapItemsObserver)
        mapViewModel.dayControls.observe(viewLifecycleOwner, dayControlsStateObserver)
        mapViewModel.boundingBox.observe(viewLifecycleOwner, boundingBoxObserver)
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
        viewBinding.mapView.map?.removeCameraListener(cameraListener)
        super.onDestroyView()
        mapViewModel.unSubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFragmentResultListener(BUILDING_ITEM)
    }
}