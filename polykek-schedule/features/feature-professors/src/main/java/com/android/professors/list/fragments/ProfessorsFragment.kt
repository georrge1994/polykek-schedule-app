package com.android.professors.list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.professors.Professor
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.professors.R
import com.android.feature.professors.databinding.FragmentProfessorsBinding
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.professors.base.adapters.IProfessorActions
import com.android.professors.base.adapters.ProfessorRecyclerViewAdapter
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.professors.base.dagger.ProfessorsComponentHolder
import com.android.professors.list.mvi.ProfessorAction
import com.android.professors.list.mvi.ProfessorIntent
import com.android.professors.list.mvi.ProfessorState
import com.android.professors.list.viewModels.ProfessorsViewModel
import com.android.professors.search.fragments.ProfessorSearchFragment
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * Professors fragment is 4th base tab.
 *
 * @constructor Create empty constructor for professors fragment
 */
internal class ProfessorsFragment :
    ToolbarFragment<ProfessorIntent, ProfessorState, ProfessorAction, ProfessorsViewModel>() {
    private val fragmentProfessorsBinding by viewBinding(FragmentProfessorsBinding::bind)
    private lateinit var adapter: ProfessorRecyclerViewAdapter

    @Inject
    lateinit var professorsNavigationActions: IProfessorsNavigationActions

    private val clickListener = object : IProfessorActions {
        override fun onClick(professor: Professor) {
            ProfessorIntent.OpenProfessorScheduleScreen(professor).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = ProfessorsComponentHolder.getComponent()

    override fun injectToComponent() = ProfessorsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        adapter = ProfessorRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professors, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewModel.asyncSubscribe()
        fragmentProfessorsBinding.toolbarLayout.toolbar.updateToolbar(R.string.professors_fragment_title, false)
        fragmentProfessorsBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: ProfessorState) {
        super.invalidateUi(state)
        adapter.updateItems(state.professors)
        fragmentProfessorsBinding.listIsEmpty.isVisible = state.professors.isEmpty()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.professor_toolbar, menu)

    override fun onMenuItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.faq -> {
            openFAQFragment()
            true
        }
        R.id.globalSearch -> {
            openProfessorSearchFragment()
            true
        }
        else -> super.onMenuItemSelected(item)
    }

    override fun executeSingleAction(action: ProfessorAction) {
        super.executeSingleAction(action)
        when (action) {
            ProfessorAction.OpenFAQScreen -> openFAQFragment()
            ProfessorAction.OpenProfessorSearchScreen -> openProfessorSearchFragment()
            is ProfessorAction.OpenProfessorScheduleScreen -> showProfessorSchedule(action.professor)
        }
    }

    /**
     * Open FAQ fragment.
     */
    private fun openFAQFragment() = mainRouter.navigateTo(
        PolytechFragmentScreen {
            professorsNavigationActions.getFaqFragment()
        }
    )

    /**
     * Open professor search fragment.
     */
    private fun openProfessorSearchFragment() = tabRouter?.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.SEARCH)) {
            ProfessorSearchFragment()
        }
    )

    /**
     * Show professor schedule.
     *
     * @param professor Professor
     */
    private fun showProfessorSchedule(professor: Professor) = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            professorsNavigationActions.getProfessorScheduleFragment(professor.id, professor.shortName)
        }
    )

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}