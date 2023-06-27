package com.android.feature.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.VerticalFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.welcome.R
import com.android.feature.welcome.adapter.WelcomeViewPagerAdapter
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.feature.welcome.databinding.FragmentWelcomeBinding
import com.android.feature.welcome.mvi.WelcomeAction
import com.android.feature.welcome.mvi.WelcomeIntent
import com.android.feature.welcome.mvi.WelcomeState
import com.android.feature.welcome.viewModels.WelcomeViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Welcome fragment.
 *
 * @constructor Create empty constructor for welcome fragment
 */
internal class WelcomeFragment : VerticalFragment<WelcomeIntent, WelcomeState, WelcomeAction, WelcomeViewModel>() {
    private val viewBinding by viewBinding(FragmentWelcomeBinding::bind)

    private val nextBtnListener = View.OnClickListener { WelcomeIntent.ShowRoleScreen.dispatchIntent() }

    private val viewPagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            WelcomeIntent.ChangeTabPosition(position).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = WelcomeComponentHolder.getComponent()

    override fun injectToComponent() = WelcomeComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_welcome, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.viewPager2.adapter = WelcomeViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        viewBinding.viewPager2.registerOnPageChangeCallback(viewPagerListener)
        viewBinding.viewPager2.isSaveEnabled = false
        TabLayoutMediator(viewBinding.tabDots, viewBinding.viewPager2) { tab, _ ->
            viewBinding.tabDots.selectTab(tab)
        }.attach()

        viewBinding.nextBtn.setOnClickListener(nextBtnListener)
    }

    override fun invalidateUi(state: WelcomeState) {
        super.invalidateUi(state)
        viewBinding.message.text = getString(state.titleResId)
    }

    override fun executeSingleAction(action: WelcomeAction) {
        super.executeSingleAction(action)
        if (action is WelcomeAction.ShowRoleScreen) {
            showChooseRole()
        }
    }

    /**
     * Show choose role.
     */
    private fun showChooseRole() = mainRouter.navigateTo(
        PolytechFragmentScreen {
            ChooseRoleFragment()
        }
    )

    override fun onDestroyView() {
        viewBinding.viewPager2.unregisterOnPageChangeCallback(viewPagerListener)
        viewBinding.viewPager2.adapter = null
        super.onDestroyView()
    }
}