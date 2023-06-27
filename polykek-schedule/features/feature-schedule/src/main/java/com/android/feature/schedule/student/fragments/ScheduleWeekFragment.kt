package com.android.feature.schedule.student.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.ToolbarFragment
import com.android.feature.schedule.R
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schedule.databinding.FragmentWeekBinding
import com.android.feature.schedule.student.adapters.viewpager.ScheduleViewPagerAdapter
import com.android.feature.schedule.student.mvi.StudentAction
import com.android.feature.schedule.student.mvi.StudentIntent
import com.android.feature.schedule.student.mvi.StudentState
import com.android.feature.schedule.student.viewModels.ScheduleWeekViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.ui.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Schedule week fragment.
 *
 * @constructor Create empty constructor for schedule week fragment
 * @author darkt on 8/24/2017
 */
internal class ScheduleWeekFragment :
    ToolbarFragment<StudentIntent, StudentState, StudentAction, ScheduleWeekViewModel>() {
    private val viewBinding by viewBinding(FragmentWeekBinding::bind)
    private lateinit var zoomOutPageTransformer: ZoomOutPageTransformer
    private var titles: Array<String>? = null

    private val showNextWeek = View.OnClickListener { StudentIntent.ShowNextWeek.dispatchIntent() }

    private val showPreviousWeek = View.OnClickListener { StudentIntent.ShowPreviousWeek.dispatchIntent() }

    private val showCalendar = View.OnClickListener { StudentIntent.ShowDataPicker.dispatchIntent() }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        StudentIntent.ShowSpecificDate(year, month, day).dispatchIntent()
    }

    private val viewPagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            StudentIntent.SwipeDay(position).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = ScheduleComponentHolder.getComponent()

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        titles = context?.resources?.getStringArray(R.array.schedule_fragment_day_abbreviations)
        zoomOutPageTransformer = ZoomOutPageTransformer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_week, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewModel.asyncSubscribe()

        viewBinding.viewPager2.adapter = ScheduleViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.isSaveEnabled = false
        viewBinding.viewPager2.setPageTransformer(zoomOutPageTransformer)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.text = titles?.getOrNull(position)
        }.attach()

        viewBinding.scheduleToolbar.nextBtn.setOnClickListener(showNextWeek)
        viewBinding.scheduleToolbar.previousBtn.setOnClickListener(showPreviousWeek)
        viewBinding.scheduleToolbar.datesWrapper.setOnClickListener(showCalendar)
        viewBinding.viewPager2.registerOnPageChangeCallback(viewPagerListener)
    }

    override fun invalidateUi(state: StudentState) {
        super.invalidateUi(state)
        if (viewBinding.viewPager2.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
            viewBinding.viewPager2.setCurrentItem(state.viewPagerPosition, state.smoothPaging)
        }
        viewBinding.scheduleToolbar.weekName.text = state.weekTitle
        viewBinding.scheduleToolbar.date.text = state.dayTitle
        viewBinding.animation.root.isVisible = state.isLoading
    }

    override fun executeSingleAction(action: StudentAction) {
        super.executeSingleAction(action)
        if (action is StudentAction.OpenDatePicker) {
            DatePickerDialog(requireContext(), datePickerListener, action.year, action.month, action.day).show()
        }
    }

    override fun onDestroyView() {
        viewBinding.viewPager2.unregisterOnPageChangeCallback(viewPagerListener)
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}