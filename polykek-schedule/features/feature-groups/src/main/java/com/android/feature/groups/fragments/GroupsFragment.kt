package com.android.feature.groups.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.SearchToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.feature.groups.R
import com.android.feature.groups.adapters.viewpager.GroupsViewPagerAdapter
import com.android.feature.groups.dagger.GroupsComponentHolder
import com.android.feature.groups.databinding.FragmentGroupsBinding
import com.android.feature.groups.viewModels.GroupViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.ui.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

private const val SCHOOL_ID = "SCHOOL_ID"
private const val SCHOOL_ABBR = "SCHOOL_ABBR"

/**
 * Root group fragment.
 *
 * @constructor Create empty constructor for groups fragment
 */
internal class GroupsFragment : SearchToolbarFragment<GroupViewModel>(GroupViewModel::class) {
    private val viewBinding by viewBinding(FragmentGroupsBinding::bind)
    private lateinit var zoomOutPageTransformer: ZoomOutPageTransformer
    private var scheduleMode = ScheduleMode.SEARCH
    private var schoolAbbr: String? = null
    private var schoolId: String? = null

    private val loadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun getComponent(): IModuleComponent = GroupsComponentHolder.getComponent()

    override fun injectToComponent() = GroupsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zoomOutPageTransformer = ZoomOutPageTransformer()
        arguments?.apply {
            scheduleMode = getScheduleMode()
            schoolAbbr = getString(SCHOOL_ABBR)
            schoolId = getString(SCHOOL_ID)
            viewModel.updateGroups(schoolId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_groups, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(schoolAbbr, true)
        viewBinding.viewPager2.adapter = GroupsViewPagerAdapter(scheduleMode, childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.offscreenPageLimit = 2
        viewBinding.viewPager2.isSaveEnabled = false
        viewBinding.viewPager2.setPageTransformer(zoomOutPageTransformer)
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager2) { tab, position ->
            tab.text = getTitle(position)
        }.attach()

        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
    }

    /**
     * Get tab title.
     *
     * @param position Position
     * @return Title
     */
    private fun getTitle(position: Int) = when (position) {
        0 -> context?.getString(R.string.groups_fragment_bachelor)
        1 -> context?.getString(R.string.groups_fragment_master)
        else -> context?.getString(R.string.groups_fragment_other)
    }

    override fun onDestroyView() {
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
    }

    companion object {
        /**
         * Get new instance.
         *
         * @param scheduleMode Schedule mode
         * @param schoolId School id
         * @param abbr School abbr
         * @return [Bundle]
         */
        internal fun newInstance(scheduleMode: ScheduleMode, schoolId: String, abbr: String) = GroupsFragment().apply {
            arguments = getScheduleModeBundle(scheduleMode).apply {
                putString(SCHOOL_ID, schoolId)
                putString(SCHOOL_ABBR, abbr)
            }
        }
    }
}