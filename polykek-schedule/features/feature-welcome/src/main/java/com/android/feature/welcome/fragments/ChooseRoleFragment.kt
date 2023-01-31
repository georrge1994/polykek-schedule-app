package com.android.feature.welcome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.welcome.R
import com.android.feature.welcome.dagger.IWelcomeNavigationActions
import com.android.feature.welcome.dagger.WelcomeComponentHolder
import com.android.feature.welcome.databinding.FragmentChooseRoleBinding
import javax.inject.Inject

/**
 * Choose role fragment: student or professor.
 *
 * @constructor Create empty constructor for role choose fragment
 */
internal class ChooseRoleFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentChooseRoleBinding::bind)

    @Inject
    lateinit var welcomeNavigationActions: IWelcomeNavigationActions

    private val studentClickListener = View.OnClickListener { showSchools() }

    private val professorClickListener = View.OnClickListener { showProfessorSearch() }

    override fun injectToComponent() = WelcomeComponentHolder.getComponent().inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_choose_role, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.studentChoice.setOnClickListener(studentClickListener)
        viewBinding.professorChoice.setOnClickListener(professorClickListener)
    }

    /**
     * Show school list.
     */
    private fun showSchools() = mainRouter.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.WELCOME)) {
            welcomeNavigationActions.getSchools()
        }
    )

    /**
     * Show professor search.
     */
    private fun showProfessorSearch() = mainRouter.navigateTo(
        PolytechFragmentScreen(arguments = getScheduleModeBundle(ScheduleMode.WELCOME)) {
            welcomeNavigationActions.getProfessorSearch()
        }
    )
}