package com.android.professors.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.professors.Professor
import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.models.getScheduleMode
import com.android.core.ui.navigation.polytechCicirone.AnimationType
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.professors.R
import com.android.feature.professors.databinding.FragmentProfessorSearchBinding
import com.android.professors.base.adapters.IProfessorActions
import com.android.professors.base.adapters.ProfessorRecyclerViewAdapter
import com.android.professors.base.dagger.IProfessorsNavigationActions
import com.android.professors.base.dagger.ProfessorsComponentHolder
import com.android.professors.search.viewModels.ProfessorSearchViewModel
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.android.shared.code.utils.syntaxSugar.hideSoftwareKeyboard
import javax.inject.Inject

/**
 * Professor search fragment.
 *
 * @constructor Create empty constructor for professor search fragment
 */
internal class ProfessorSearchFragment : ToolbarFragment() {
    private val viewBinding by viewBinding(FragmentProfessorSearchBinding::bind)
    private lateinit var viewModel: ProfessorSearchViewModel
    private lateinit var adapter: ProfessorRecyclerViewAdapter

    @Inject
    lateinit var professorsNavigationActions: IProfessorsNavigationActions

    // Listeners.
    private val searchClickListener = View.OnClickListener { searchProfessors() }

    private val clickListener = object : IProfessorActions {
        override fun onClick(professor: Professor) {
            showNextScreen(professor)
        }
    }

    private val actionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
            searchProfessors()
        true
    }

    // Observers.
    private val professorsObserver = Observer<List<Professor>?> { adapter.updateItems(it) }

    private val listIsEmptyObserver = Observer<Boolean> { viewBinding.message.isVisible = it }

    private val messageIdObserver = Observer<Int> { viewBinding.message.text = getString(it) }

    private val loadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun injectToComponent() = ProfessorsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        viewModel.scheduleMode = arguments?.getScheduleMode() ?: ScheduleMode.SEARCH
        adapter = ProfessorRecyclerViewAdapter(requireContext(), clickListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professor_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asyncSubscribe()

        viewBinding.toolbarLayout.toolbar.updateToolbar(R.string.professors_search_fragment_search, true)
        viewBinding.recyclerView.adapter = adapter

        viewModel.professors.observe(viewLifecycleOwner, professorsObserver)
        viewModel.messageId.observe(viewLifecycleOwner, messageIdObserver)
        viewModel.listIsEmpty.observe(viewLifecycleOwner, listIsEmptyObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)

        viewBinding.searchBtn.setOnClickListener(searchClickListener)
        viewBinding.keyWord.setOnEditorActionListener(actionListener)
    }

    override fun onPause() {
        super.onPause()
        viewBinding.keyWord.hideSoftwareKeyboard()
    }

    /**
     * Search professors.
     */
    private fun searchProfessors() {
        viewModel.searchProfessorsByKeyword(viewBinding.keyWord.text.toString())
        viewBinding.keyWord.hideSoftwareKeyboard()
    }

    /**
     * Show next screen.
     *
     * @param professor Professor
     */
    private fun showNextScreen(professor: Professor) = when (viewModel.scheduleMode) {
        ScheduleMode.WELCOME -> {
            viewModel.saveSelectedItem(professor)
            mainRouter.newRootScreen(
                PolytechFragmentScreen(addToBackStack = false) {
                    professorsNavigationActions.getMainScreen()
                }
            )
        }
        ScheduleMode.NEW_ITEM -> {
            viewModel.saveSelectedItem(professor)
            mainRouter.backTo(
                PolytechFragmentScreen(addToBackStack = false, animationType = AnimationType.FROM_LEFT_TO_RIGHT) {
                    professorsNavigationActions.getMainScreen()
                }
            )
        }
        ScheduleMode.SEARCH -> {
            mainRouter.navigateTo(
                PolytechFragmentScreen {
                    professorsNavigationActions.getProfessorScheduleFragment(professor.id, professor.shortName)
                }
            )
        }
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}