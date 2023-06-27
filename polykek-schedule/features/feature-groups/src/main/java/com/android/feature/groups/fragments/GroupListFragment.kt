package com.android.feature.groups.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.groups.R
import com.android.feature.groups.adapters.recycler.GroupsRecyclerViewAdapter
import com.android.feature.groups.adapters.recycler.IGroupActions
import com.android.feature.groups.dagger.GroupsComponentHolder
import com.android.feature.groups.dagger.IGroupsNavigationActions
import com.android.feature.groups.databinding.FragmentGroupsListBinding
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.feature.groups.mvi.GroupsAction
import com.android.feature.groups.mvi.GroupsIntent
import com.android.feature.groups.mvi.GroupsState
import com.android.feature.groups.viewModels.GroupViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment
import javax.inject.Inject

private const val TAB_TYPE = "TAB_TYPE"

/**
 * Group list fragment.
 *
 * @constructor Create empty constructor for group list fragment
 */
internal class GroupListFragment : NavigationFragment<GroupsIntent, GroupsState, GroupsAction, GroupViewModel>() {
    private val viewBinding by viewBinding(FragmentGroupsListBinding::bind)
    private lateinit var adapter: GroupsRecyclerViewAdapter
    private var scheduleMode = ScheduleMode.SEARCH
    private var tabType: GroupType? = null

    @Inject
    lateinit var groupsNavigationActions: IGroupsNavigationActions

    private val groupActions = object : IGroupActions {
        override fun onClick(group: Group) {
            GroupsIntent.GroupSelected(group, tabType!!).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = GroupsComponentHolder.getComponent()

    override fun injectToComponent() = GroupsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createSharedViewModelWithParentFragment(viewModelFactory)
        adapter = GroupsRecyclerViewAdapter(requireContext(), groupActions)
        arguments?.apply {
            scheduleMode = getScheduleMode()
            tabType = GroupType.values()[getInt(TAB_TYPE)]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_groups_list, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: GroupsState) {
        super.invalidateUi(state)
        viewBinding.listIsEmpty.isVisible = state.isLoading.not() && state.items?.get(tabType).isNullOrEmpty()
        adapter.updateItems(state.items?.get(tabType))
    }

    override fun executeSingleAction(action: GroupsAction) {
        super.executeSingleAction(action)
        if (action is GroupsAction.SelectGroup && action.groupType == tabType) {
            showNextFragment()
        }
    }

    /**
     * Show next fragment.
     */
    private fun showNextFragment() = if (scheduleMode == ScheduleMode.WELCOME) {
        mainRouter.newRootScreen(
            PolytechFragmentScreen {
                groupsNavigationActions.getMainFragment()
            }
        )
    } else {
        mainRouter.newRootScreen(
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.FROM_LEFT_TO_RIGHT) {
                groupsNavigationActions.getMainFragment()
            }
        )
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }

    companion object {
        /**
         * New instance.
         *
         * @param scheduleMode Schedule mode
         * @param type Type
         * @return [GroupListFragment]
         */
        internal fun newInstance(scheduleMode: ScheduleMode, type: GroupType) = GroupListFragment().apply {
            arguments = getScheduleModeBundle(scheduleMode).apply {
                putInt(TAB_TYPE, type.ordinal)
            }
        }
    }
}