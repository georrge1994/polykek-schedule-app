package com.android.feature.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.BaseFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.mvi.MviFragment
import com.android.core.ui.navigation.ICiceroneHolder
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.welcome.R
import com.android.feature.welcome.dagger.IWelcomeNavigationActions
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.feature.welcome.databinding.FragmentChooseRoleBinding
import com.android.feature.welcome.mvi.WelcomeAction
import com.android.feature.welcome.mvi.WelcomeIntent
import com.android.feature.welcome.mvi.WelcomeState
import com.android.feature.welcome.viewModels.WelcomeViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * Choose role fragment: student or professor.
 *
 * @constructor Create empty constructor for role choose fragment
 */
internal class ChooseRoleFragment : MviFragment<WelcomeIntent, WelcomeState, WelcomeAction, WelcomeViewModel>() {
    private val viewBinding by viewBinding(FragmentChooseRoleBinding::bind)

    @Inject
    lateinit var ciceroneHolder: ICiceroneHolder

    @Inject
    lateinit var welcomeNavigationActions: IWelcomeNavigationActions

    private val studentClickListener = View.OnClickListener { WelcomeIntent.ShowSchoolScreen.dispatchIntent() }

    private val professorClickListener = View.OnClickListener { WelcomeIntent.ShowProfessorScreen.dispatchIntent() }

    override fun getComponent(): IModuleComponent = WelcomeComponentHolder.getComponent()

    override fun injectToComponent() = WelcomeComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_choose_role, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.studentChoice.setOnClickListener(studentClickListener)
        viewBinding.professorChoice.setOnClickListener(professorClickListener)
    }

    override fun executeSingleAction(action: WelcomeAction) {
        super.executeSingleAction(action)
        if (action is WelcomeAction.ShowProfessorScreen) {
            showProfessorSearch()
        } else if (action is WelcomeAction.ShowSchoolScreen) {
            showSchools()
        }
    }

    /**
     * Show school list.
     */
    private fun showSchools() = ciceroneHolder.getMainCicerone().router.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.WELCOME)) {
            welcomeNavigationActions.getSchools()
        }
    )

    /**
     * Show professor search.
     */
    private fun showProfessorSearch() = ciceroneHolder.getMainCicerone().router.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.WELCOME)) {
            welcomeNavigationActions.getProfessorSearch()
        }
    )
}