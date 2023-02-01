package com.android.feature.main.screen.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.feature.main.screen.databinding.FragmentMainBinding
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
internal class MainFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentMainBinding::bind)
    private lateinit var viewModel: BottomAnimationViewModel

    private val onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        selectTab(item.itemId)
    }

    private val slideTopPositionObserver = Observer<Pair<Float, Float>> {
        with(viewBinding) {
            bottomNavigationView.translationY = it.first * bottomNavigationView.height
            bottomNavigationView.visibility = View.VISIBLE
            bottomNavigationView.alpha = it.second
        }
    }

    private val slideBottomPositionObserver = Observer<Pair<Float, Float>> {
        viewBinding.bottomNavigationView.alpha = 0f
        viewBinding.bottomNavigationView.visibility = View.INVISIBLE
    }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateBottomAnimation(isOpen = false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener)
        viewBinding.bottomNavigationView.selectedItemId = currentItemId
        if (context.isPortraitMode()) {
            viewModel.slideTopPosition.observe(viewLifecycleOwner, slideTopPositionObserver)
            viewModel.slideBottomPosition.observe(viewLifecycleOwner, slideBottomPositionObserver)
        }
    }

    /**
     * Set to main container one of [TabContainerFragment].
     *
     * @param itemId Item id
     * @return Was tab selected or not
     */
    private fun selectTab(itemId: Int): Boolean = with(childFragmentManager) {
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

    companion object {
        private var currentItemId: Int = R.id.schedule_navigation
    }
}