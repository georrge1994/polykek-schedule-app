package com.android.feature.schedule.professor.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.ToolbarFragment
import com.android.feature.schedule.R
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schedule.databinding.FragmentProfessorScheduleBinding
import com.android.feature.schedule.professor.adapters.ProfessorLessonsRecyclerViewAdapter
import com.android.feature.schedule.professor.mvi.ProfessorAction
import com.android.feature.schedule.professor.mvi.ProfessorIntent
import com.android.feature.schedule.professor.mvi.ProfessorState
import com.android.feature.schedule.professor.viewModels.ProfessorsScheduleViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel

private const val PROFESSOR_ID = "PROFESSOR_ID"
private const val PROFESSOR_TITLE = "PROFESSOR"

/**
 * Professor schedule fragment.
 *
 * @constructor Create empty constructor for professor schedule fragment
 */
internal class ProfessorScheduleFragment :
    ToolbarFragment<ProfessorIntent, ProfessorState, ProfessorAction, ProfessorsScheduleViewModel>() {
    private val viewBinding by viewBinding(FragmentProfessorScheduleBinding::bind)
    private lateinit var adapter: ProfessorLessonsRecyclerViewAdapter

    private val showNextWeek = View.OnClickListener { ProfessorIntent.ShowNextWeek.dispatchIntent() }

    private val showPreviousWeek = View.OnClickListener { ProfessorIntent.ShowPreviousWeek.dispatchIntent() }

    private val showCalendar = View.OnClickListener { ProfessorIntent.ShowDataPicker.dispatchIntent() }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        ProfessorIntent.ShowSpecificDate(year, month, day).dispatchIntent()
    }

    override fun getComponent(): IModuleComponent = ScheduleComponentHolder.getComponent()

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
        ProfessorIntent.UpdateProfessorId(arguments?.getInt(PROFESSOR_ID)).dispatchIntent()
        adapter = ProfessorLessonsRecyclerViewAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professor_schedule, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        ProfessorIntent.CheckPeriodAndRefresh.dispatchIntent()
        viewBinding.toolbarLayout.toolbar.updateToolbar(
            title = getString(R.string.professors_fragment_schedule_of, arguments?.getString(PROFESSOR_TITLE)),
            showBackBtn = true
        )
        viewBinding.recyclerView.adapter = adapter
        viewBinding.scheduleToolbar.nextBtn.setOnClickListener(showNextWeek)
        viewBinding.scheduleToolbar.previousBtn.setOnClickListener(showPreviousWeek)
        viewBinding.scheduleToolbar.datesWrapper.setOnClickListener(showCalendar)
    }

    override fun invalidateUi(state: ProfessorState) {
        super.invalidateUi(state)
        viewBinding.scheduleToolbar.weekName.text = state.weekTitle
        viewBinding.animation.root.isVisible = state.isLoading
        viewBinding.lessonsListIsEmpty.isVisible = state.lessonsAndHeaders.isEmpty() && !state.isLoading
        adapter.updateItems(state.lessonsAndHeaders)
    }

    override fun executeSingleAction(action: ProfessorAction) {
        super.executeSingleAction(action)
        if (action is ProfessorAction.OpenDatePicker) {
            DatePickerDialog(requireContext(), datePickerListener, action.year, action.month, action.day).show()
        }
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }

    internal companion object {
        /**
         * Get bundle.
         *
         * @param id Professor id
         * @param title Professor title
         * @return [ProfessorScheduleFragment]
         */
        internal fun newInstance(id: Int, title: String?): ProfessorScheduleFragment =
            ProfessorScheduleFragment().apply {
                arguments = Bundle().apply {
                    putInt(PROFESSOR_ID, id)
                    putString(PROFESSOR_TITLE, title)
                }
            }
    }
}