package com.android.feature.main.screen.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.main.screen.databinding.FragmentMainBinding
import com.android.feature.main.screen.menu.mvi.MenuAction
import com.android.feature.main.screen.menu.mvi.MenuIntent
import com.android.feature.main.screen.menu.mvi.MenuState
import com.android.feature.main.screen.menu.viewModels.BottomAnimationViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.isPortraitMode
import com.google.android.material.navigation.NavigationBarView

/**
 * Main fragment.
 *
 * @constructor Create empty constructor for main fragment
 */
internal class MainFragment : NavigationFragment<MenuIntent, MenuState, MenuAction, BottomAnimationViewModel>() {
    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    private val onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        selectTab(item.itemId)
    }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener)
        viewBinding.bottomNavigationView.selectedItemId = currentItemId
    }

    override fun invalidateUi(state: MenuState) {
        super.invalidateUi(state)
        if (context.isPortraitMode()) {
            viewBinding.bottomNavigationView.translationY =
                state.bottomNavigationViewShift * viewBinding.bottomNavigationView.height
            viewBinding.bottomNavigationView.visibility = state.bottomNavigationViewVisibility
            viewBinding.bottomNavigationView.alpha = state.bottomNavigationViewAlpha
        }
    }

    /**
     * Set to main container one of [TabContainerFragment].
     *
     * @param itemId Item id
     * @return Was tab selected or not
     */
    private fun selectTab(itemId: Int): Boolean = with(childFragmentManager) {
        // Don't do anything if the state is state has already been saved.
        if (childFragmentManager.isStateSaved)
            return false
        val tabTag = itemId.toString()
        val newSelectedTab = findFragmentByTag(tabTag)
        val currentTab = findFragmentByTag(currentItemId.toString())
        if (currentTab != null && currentTab == newSelectedTab) {
            return false
        }
        val transaction = beginTransaction()
        if (newSelectedTab == null) {
            transaction.add(
                R.id.navigationHostContainer,
                TabContainerFragment.getNewInstance(itemId),
                tabTag
            )
        }
        if (currentTab != null) {
            transaction.detach(currentTab)
        }
        if (newSelectedTab != null) {
            transaction.attach(newSelectedTab)
        }
        transaction.setReorderingAllowed(true)
            .commitNow()
        currentItemId = itemId
        return true
    }

    override fun onDestroyView() {
        viewBinding.bottomNavigationView.setOnItemSelectedListener(null)
        super.onDestroyView()
    }

    private companion object {
        private var currentItemId: Int = R.id.schedule_navigation
    }
}