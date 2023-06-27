package com.android.feature.schools.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.android.feature.schools.mvi.SchoolAction
import com.android.feature.schools.mvi.SchoolIntent
import com.android.feature.schools.mvi.SchoolState
import com.android.feature.schools.viewModels.SchoolViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * Schools list fragment.
 *
 * @constructor Create empty constructor for schools list fragment
 */
internal class SchoolsFragment : ToolbarFragment<SchoolIntent, SchoolState, SchoolAction, SchoolViewModel>() {
    private val viewBinding by viewBinding(FragmentSchoolsBinding::bind)
    private lateinit var adapter: SchoolsRecyclerViewAdapter

    @Inject
    lateinit var schoolsNavigationActions: ISchoolsNavigationActions

    private val schoolActions = object : ISchoolActions {
        override fun onClick(school: School) {
            SchoolIntent.ShowGroups(school).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = SchoolsComponentHolder.getComponent()

    override fun injectToComponent() = SchoolsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        SchoolIntent.InitContent(arguments?.getScheduleMode() ?: ScheduleMode.SEARCH).dispatchIntent()
        adapter = SchoolsRecyclerViewAdapter(requireContext(), schoolActions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_schools, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.school_fragment_list_of_schools, true)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: SchoolState) {
        super.invalidateUi(state)
        adapter.updateItems(state.schools)
        viewBinding.animation.root.isVisible = state.isLoading
    }

    override fun executeSingleAction(action: SchoolAction) {
        super.executeSingleAction(action)
        if (action is SchoolAction.ShowGroups) {
            showGroupsFragment(action.school, action.scheduleMode)
        }
    }

    /**
     * Show groups fragment.
     *
     * @param school School
     * @param scheduleMode Schedule mode
     */
    private fun showGroupsFragment(school: School, scheduleMode: ScheduleMode) = mainRouter.navigateTo(
        PolytechFragmentScreen {
            schoolsNavigationActions.getGroupsScreen(scheduleMode, school.id, school.abbr)
        }
    )

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}