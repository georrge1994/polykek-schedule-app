package com.android.feature.schools.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleMode
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.schools.R
import com.android.feature.schools.adapters.ISchoolActions
import com.android.feature.schools.adapters.SchoolsRecyclerViewAdapter
import com.android.feature.schools.dagger.ISchoolsNavigationActions
import com.android.feature.schools.dagger.SchoolsComponentHolder
import com.android.feature.schools.databinding.FragmentSchoolsBinding
import com.android.feature.schools.models.School
import com.android.feature.schools.viewModels.SchoolViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * Schools list fragment.
 *
 * @constructor Create empty constructor for schools list fragment
 */
internal class SchoolsFragment : ToolbarFragment() {
    private val viewBinding by viewBinding(FragmentSchoolsBinding::bind)
    private lateinit var adapter: SchoolsRecyclerViewAdapter
    private lateinit var schoolViewModel: SchoolViewModel

    @Inject
    lateinit var schoolsNavigationActions: ISchoolsNavigationActions

    private val schoolActions = object : ISchoolActions {
        override fun onClick(school: School) {
            showGroupsFragment(school)
        }
    }

    private val schoolsObserver = Observer<List<School>?> { adapter.updateItems(it) }

    private val isLoadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun getComponent(): IModuleComponent = SchoolsComponentHolder.getComponent()

    override fun injectToComponent() = SchoolsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schoolViewModel = createViewModel(viewModelFactory)
        schoolViewModel.updateSchools()
        schoolViewModel.scheduleMode = arguments?.getScheduleMode() ?: ScheduleMode.SEARCH
        adapter = SchoolsRecyclerViewAdapter(requireContext(), schoolActions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_schools, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.school_fragment_list_of_schools, true)
        viewBinding.recyclerView.adapter = adapter
        schoolViewModel.schools.observe(viewLifecycleOwner, schoolsObserver)
        schoolViewModel.isLoading.observe(viewLifecycleOwner, isLoadingObserver)
    }

    /**
     * Show groups fragment.
     *
     * @param school School
     */
    private fun showGroupsFragment(school: School) = mainRouter.navigateTo(
        PolytechFragmentScreen {
            schoolsNavigationActions.getGroupsScreen(schoolViewModel.scheduleMode, school.id, school.abbr)
        }
    )

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}