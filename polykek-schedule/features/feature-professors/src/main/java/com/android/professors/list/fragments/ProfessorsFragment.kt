package com.android.professors.list.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.professors.Professor
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleModeBundle
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.professors.R
import com.android.feature.professors.databinding.FragmentProfessorsBinding
import com.android.professors.base.adapters.IProfessorActions
import com.android.professors.base.adapters.ProfessorRecyclerViewAdapter
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.professors.base.dagger.ProfessorsComponentHolder
import com.android.professors.list.viewModels.ProfessorsViewModel
import com.android.professors.search.fragments.ProfessorSearchFragment
import com.android.shared.code.utils.syntaxSugar.createViewModel
import javax.inject.Inject

/**
 * Professors fragment is 4th base tab.
 *
 * @constructor Create empty constructor for professors fragment
 */
internal class ProfessorsFragment : ToolbarFragment() {
    private val fragmentProfessorsBinding by viewBinding(FragmentProfessorsBinding::bind)
    private lateinit var professorsViewModel: ProfessorsViewModel
    private lateinit var adapter: ProfessorRecyclerViewAdapter

    @Inject
    lateinit var professorsNavigationActions: IProfessorsNavigationActions

    private val clickListener = object : IProfessorActions {
        override fun onClick(professor: Professor) {
            showProfessorSchedule(professor)
        }
    }

    private val buildingsObserver = Observer<List<Professor>?> { adapter.updateItems(it) }

    private val listIsEmptyObserver = Observer<Boolean> { fragmentProfessorsBinding.listIsEmpty.isVisible = it }

    override fun injectToComponent() = ProfessorsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        professorsViewModel = createViewModel(viewModelFactory)
        adapter = ProfessorRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professors, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        professorsViewModel.asyncSubscribe()
        fragmentProfessorsBinding.toolbarLayout.toolbar.updateToolbar(R.string.professors_fragment_title, false)
        fragmentProfessorsBinding.recyclerView.adapter = adapter

        professorsViewModel.professors.observe(viewLifecycleOwner, buildingsObserver)
        professorsViewModel.isListEmpty.observe(viewLifecycleOwner, listIsEmptyObserver)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.professor_toolbar, menu)

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.faq -> openFAQFragment()
            R.id.globalSearch -> openProfessorSearchFragment()
        }
        return super.onMenuItemSelected(item)
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
        professorsViewModel.unSubscribe()
    }
}