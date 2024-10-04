package com.android.feature.main.screen.saved.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.savedItems.SavedItem
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.IMainScreenNavigationActions
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.main.screen.databinding.FragmentSavedItemsBinding
import com.android.feature.main.screen.menu.mvi.MenuAction
import com.android.feature.main.screen.menu.mvi.MenuIntent
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.feature.main.screen.saved.adapters.ISaveItemMenuActions
import com.android.feature.main.screen.saved.adapters.SavedItemsRecyclerViewAdapter
import com.android.feature.main.screen.saved.mvi.SavedItemAction
import com.android.feature.main.screen.saved.mvi.SavedItemIntent
import com.android.feature.main.screen.saved.mvi.SavedItemState
import com.android.feature.main.screen.saved.viewModels.SavedItemsViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.email.OpenEmailChooserUseCase
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

private const val VND_ANDROID_CURSOR_ITEM_EMAIL = "vnd.android.cursor.item/email"

/**
 * Saved items fragment.
 *
 * @constructor Create empty constructor for saved items fragment
 */
internal class SavedItemsFragment :
    NavigationFragment<SavedItemIntent, SavedItemState, SavedItemAction, SavedItemsViewModel>() {
    private val viewBinding by viewBinding(FragmentSavedItemsBinding::bind)
    private lateinit var bottomAnimationViewModel: BottomAnimationViewModel
    private lateinit var adapter: SavedItemsRecyclerViewAdapter

    @Inject
    lateinit var mainScreenNavigationActions: IMainScreenNavigationActions

    @Inject
    lateinit var openEmailChooserUseCase: OpenEmailChooserUseCase

    private val savedItemActions = object : ISaveItemMenuActions {
        override fun onClick(item: SavedItem) {
            bottomAnimationViewModel.dispatchIntentAsync(
                MenuIntent.ChangeStateOfBottomBar(BottomSheetBehavior.STATE_COLLAPSED))
            SavedItemIntent.SelectItem(item).dispatchIntent()
        }

        override fun onRemove(item: SavedItem) {
            SavedItemIntent.RemoveItem(item).dispatchIntent()
        }

        override fun addGroup() {
            SavedItemIntent.OpenSchools.dispatchIntent()
        }

        override fun addProfessor() {
            SavedItemIntent.OpenProfessors.dispatchIntent()
        }

        override fun sayAboutScheduleError() {
            SavedItemIntent.OpenEmailChooser.dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        bottomAnimationViewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
        adapter = SavedItemsRecyclerViewAdapter(requireContext(), savedItemActions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_saved_items, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewModel.asyncSubscribe()
        viewBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: SavedItemState) {
        super.invalidateUi(state)
        adapter.updateItems(state.menuItems)
        viewBinding.isEmpty.isVisible = state.menuItems.isEmpty()
    }

    override fun executeSingleAction(action: SavedItemAction) {
        super.executeSingleAction(action)
        when (action) {
            is SavedItemAction.OpenEmailChooser -> openEmailChooser(action.groupName)
            SavedItemAction.OpenProfessors -> showProfessorSearch()
            SavedItemAction.OpenSchools -> showSchools()
        }
    }

    /**
     * Show schools.
     */
    private fun showSchools() = mainRouter.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.NEW_ITEM)) {
            mainScreenNavigationActions.getSchoolsFragment()
        }
    )

    /**
     * Show professor search.
     */
    private fun showProfessorSearch() = mainRouter.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.NEW_ITEM)) {
            mainScreenNavigationActions.getProfessorSearchFragment()
        }
    )

    /**
     * Open email chooser.
     *
     * @param groupName Group name
     */
    private fun openEmailChooser(groupName: String) {
        // Close bottom sheet bar.
        (activity as AppCompatActivity).createViewModel<BottomAnimationViewModel>(viewModelFactory)
            .dispatchIntentAsync(MenuIntent.ChangeStateOfBottomBar(BottomSheetBehavior.STATE_COLLAPSED))
        // Open email chooser.
        openEmailChooserUseCase.openEmailChooser(
            requireContext(),
            getString(R.string.error_in_the_schedule_email),
            getString(R.string.error_in_the_schedule_subject, groupName),
            getString(R.string.error_in_the_schedule_selection_message)
        )
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}