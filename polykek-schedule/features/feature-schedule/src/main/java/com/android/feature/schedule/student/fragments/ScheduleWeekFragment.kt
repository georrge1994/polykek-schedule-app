package com.android.feature.schedule.student.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.feature.schedule.R
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schedule.base.fragments.BaseScheduleFragment
import com.android.feature.schedule.databinding.FragmentWeekBinding
import com.android.feature.schedule.student.adapters.viewpager.ScheduleViewPagerAdapter
import com.android.feature.schedule.student.viewModels.ScheduleWeekViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.ui.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Schedule week fragment.
 *
 * @constructor Create empty constructor for schedule week fragment
 * @author darkt on 8/24/2017
 */
internal class ScheduleWeekFragment : BaseScheduleFragment<ScheduleWeekViewModel>(ScheduleWeekViewModel::class) {
    private val viewBinding by viewBinding(FragmentWeekBinding::bind)
    private lateinit var zoomOutPageTransformer: ZoomOutPageTransformer
    private var titles: Array<String>? = null

    private val viewPagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.swipeDay(position)
        }
    }

    private val selectedDayObserver = Observer<Pair<Int, Boolean>> {
        viewBinding.viewPager2.setCurrentItem(it.first, it.second)
    }

    private val weekTitleObserver = Observer<String> { viewBinding.scheduleToolbar.weekName.text = it }

    private val dayTitleObserver = Observer<String> { viewBinding.scheduleToolbar.date.text = it }

    private val loadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun getComponent(): IModuleComponent = ScheduleComponentHolder.getComponent()

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titles = context?.resources?.getStringArray(R.array.schedule_fragment_day_abbreviations)
        zoomOutPageTransformer = ZoomOutPageTransformer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_week, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewPager2.adapter = ScheduleViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.isSaveEnabled = false
        viewBinding.viewPager2.setPageTransformer(zoomOutPageTransformer)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.text = titles?.getOrNull(position)
        }.attach()

        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.weekTitle.observe(viewLifecycleOwner, weekTitleObserver)
        viewModel.dayTitleLiveData.observe(viewLifecycleOwner, dayTitleObserver)
        viewModel.viewPagerPosition.observe(viewLifecycleOwner, selectedDayObserver)

        viewBinding.scheduleToolbar.nextBtn.setOnClickListener(showNextWeek)
        viewBinding.scheduleToolbar.previousBtn.setOnClickListener(showPreviousWeek)
        viewBinding.scheduleToolbar.datesWrapper.setOnClickListener(showCalendar)
        viewBinding.viewPager2.registerOnPageChangeCallback(viewPagerListener)
    }

    override fun onDestroyView() {
        viewBinding.viewPager2.unregisterOnPageChangeCallback(viewPagerListener)
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
    }
}