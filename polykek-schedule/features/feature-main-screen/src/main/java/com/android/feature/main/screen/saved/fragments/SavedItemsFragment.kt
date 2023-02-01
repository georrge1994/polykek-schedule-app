package com.android.feature.main.screen.saved.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.feature.main.screen.saved.adapters.ISaveItemMenuActions
import com.android.feature.main.screen.saved.adapters.SavedItemsRecyclerViewAdapter
import com.android.feature.main.screen.saved.viewModels.SavedItemsViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

private const val VND_ANDROID_CURSOR_ITEM_EMAIL = "vnd.android.cursor.item/email"

/**
 * Saved items fragment.
 *
 * @constructor Create empty constructor for saved items fragment
 */
internal class SavedItemsFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentSavedItemsBinding::bind)
    private lateinit var adapter: SavedItemsRecyclerViewAdapter
    private lateinit var savedItemsViewModel: SavedItemsViewModel
    private lateinit var bottomAnimationViewModel: BottomAnimationViewModel

    @Inject
    lateinit var mainScreenNavigationActions: IMainScreenNavigationActions

    private val savedItemActions = object : ISaveItemMenuActions {
        override fun onClick(item: SavedItem) {
            bottomAnimationViewModel.updateBottomAnimation(isOpen = false)
            savedItemsViewModel.selectItem(item)
        }

        override fun onRemove(item: SavedItem) {
            savedItemsViewModel.delete(item)
        }

        override fun addGroup() {
            showSchools()
        }

        override fun addProfessor() {
            showProfessorSearch()
        }

        override fun sayAboutScheduleError() {
            openEmailChooser()
        }
    }

    private val savedItemsObserver = Observer<List<Any>> { adapter.updateItems(it) }

    private val isEmptyObserver = Observer<Boolean> { viewBinding.isEmpty.isVisible = it }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedItemsViewModel = createViewModel(viewModelFactory)
        bottomAnimationViewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
        adapter = SavedItemsRecyclerViewAdapter(requireContext(), savedItemActions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_saved_items, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
        savedItemsViewModel.isEmpty.observe(viewLifecycleOwner, isEmptyObserver)
        savedItemsViewModel.items.observe(viewLifecycleOwner, savedItemsObserver)
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
     */
    private fun openEmailChooser() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = VND_ANDROID_CURSOR_ITEM_EMAIL
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.error_in_the_schedule_email)))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.error_in_the_schedule_subject, savedItemsViewModel.getSelectedItem()))
        startActivity(Intent.createChooser(emailIntent, getString(R.string.error_in_the_schedule_selection_message)))
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }
}