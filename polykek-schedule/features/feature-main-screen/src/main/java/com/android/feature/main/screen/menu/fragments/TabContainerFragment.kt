package com.android.feature.main.screen.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.android.core.ui.fragments.BaseFragment
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.core.ui.navigation.IRouterProvider
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechAppNavigator
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.main.screen.R
import com.android.feature.main.screen.dagger.IMainScreenNavigationActions
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

private const val TAB_CONTAINER_ID = "TAB_CONTAINER_ID"

/**
 * Every each tab container fragment has own router and provides independent navigation for own tab.
 *
 * @constructor Create empty constructor for tab container fragment
 */
internal class TabContainerFragment : BaseFragment(), IRouterProvider {
    private val navigator: Navigator by lazy {
        PolytechAppNavigator(requireActivity(), R.id.ftcContainer, childFragmentManager)
    }

    @Inject
    lateinit var ciceroneHolder: ICiceroneHolder

    @Inject
    lateinit var mainScreenNavigationActions: IMainScreenNavigationActions

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(arguments?.getInt(TAB_CONTAINER_ID).toString())

    override val router: Router
        get() = cicerone.router

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            router.exit()
        }
    }

    override fun getComponent(): IModuleComponent = MainScreenComponentHolder.getComponent()

    override fun injectToComponent() = MainScreenComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.ftcContainer) == null) {
            showTabInContainer()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_tab_container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    /**
     * Show tab.
     */
    private fun showTabInContainer() = when (arguments?.getInt(TAB_CONTAINER_ID)) {
        R.id.schedule_navigation ->
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
                mainScreenNavigationActions.getScheduleFragment()
            }
        R.id.notes_navigation ->
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
                mainScreenNavigationActions.getNotesFragment()
            }
        R.id.map_navigation ->
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
                mainScreenNavigationActions.getMapFragment()
            }
        R.id.professors_navigation ->
            PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.WITHOUT) {
                mainScreenNavigationActions.getProfessorFragment()
            }
        else -> null
    }?.apply {
        router.replaceScreen(this)
    }

    companion object {
        /**
         * Get new instance.
         *
         * @param itemId Menu item id
         * @return [TabContainerFragment]
         */
        fun getNewInstance(itemId: Int): TabContainerFragment = TabContainerFragment().apply {
            arguments = Bundle().apply {
                putInt(TAB_CONTAINER_ID, itemId)
            }
        }
    }
}