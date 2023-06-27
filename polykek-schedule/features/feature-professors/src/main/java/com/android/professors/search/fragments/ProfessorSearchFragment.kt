package com.android.professors.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.professors.Professor
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleMode
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.professors.R
import com.android.feature.professors.databinding.FragmentProfessorSearchBinding
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.professors.base.adapters.IProfessorActions
import com.android.professors.base.adapters.ProfessorRecyclerViewAdapter
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.professors.base.dagger.ProfessorsComponentHolder
import com.android.professors.search.mvi.ProfessorsSearchAction
import com.android.professors.search.mvi.ProfessorsSearchIntent
import com.android.professors.search.mvi.ProfessorsSearchState
import com.android.professors.search.viewModels.ProfessorSearchViewModel
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard
import javax.inject.Inject

/**
 * Professor search fragment.
 *
 * @constructor Create empty constructor for professor search fragment
 */
internal class ProfessorSearchFragment :
    ToolbarFragment<ProfessorsSearchIntent, ProfessorsSearchState, ProfessorsSearchAction, ProfessorSearchViewModel>() {
    private val viewBinding by viewBinding(FragmentProfessorSearchBinding::bind)
    private lateinit var adapter: ProfessorRecyclerViewAdapter

    @Inject
    lateinit var professorsNavigationActions: IProfessorsNavigationActions

    // Listeners.
    private val searchClickListener = View.OnClickListener { searchProfessors() }

    private val clickListener = object : IProfessorActions {
        override fun onClick(professor: Professor) {
            ProfessorsSearchIntent.SaveAndShowNextScreen(professor).dispatchIntent()
        }
    }

    private val actionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
            searchProfessors()
        true
    }

    override fun getComponent(): IModuleComponent = ProfessorsComponentHolder.getComponent()

    override fun injectToComponent() = ProfessorsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        ProfessorsSearchIntent.InitContent(arguments?.getScheduleMode() ?: ScheduleMode.SEARCH).dispatchIntent()
        adapter = ProfessorRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professor_search, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.professors_search_fragment_search, true)
        viewBinding.recyclerView.adapter = adapter
        viewBinding.searchBtn.setOnClickListener(searchClickListener)
        viewBinding.keyWord.setOnEditorActionListener(actionListener)
    }

    override fun invalidateUi(state: ProfessorsSearchState) {
        super.invalidateUi(state)
        adapter.updateItems(state.professors)
        viewBinding.animation.root.isVisible = state.isLoading
        viewBinding.message.isVisible = state.isLoading.not() && state.professors.isEmpty()
        viewBinding.message.text = getString(state.messageId)
    }

    override fun executeSingleAction(action: ProfessorsSearchAction) {
        super.executeSingleAction(action)
        if (action is ProfessorsSearchAction.ShowNextScreen) {
            showNextScreen(action.scheduleMode, action.professor)
        }
    }

    override fun onPause() {
        super.onPause()
        viewBinding.keyWord.hideSoftwareKeyboard()
    }

    /**
     * Search professors.
     */
    private fun searchProfessors() {
        ProfessorsSearchIntent.SearchProfessorsByKeyword(viewBinding.keyWord.text.toString()).dispatchIntent()
        viewBinding.keyWord.hideSoftwareKeyboard()
    }

    /**
     * Show the next screen.
     *
     * @param scheduleMode Schedule mode
     * @param professor Professor
     */
    private fun showNextScreen(scheduleMode: ScheduleMode, professor: Professor) = when (scheduleMode) {
        ScheduleMode.WELCOME -> mainRouter.newRootScreen(
            PolytechFragmentScreen(addToBackStack = false) {
                professorsNavigationActions.getMainScreen()
            }
        )
        ScheduleMode.NEW_ITEM -> mainRouter.newRootScreen(
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.FROM_LEFT_TO_RIGHT) {
                professorsNavigationActions.getMainScreen()
            }
        )
        ScheduleMode.SEARCH -> mainRouter.navigateTo(
            PolytechFragmentScreen {
                professorsNavigationActions.getProfessorScheduleFragment(professor.id, professor.shortName)
            }
        )
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}