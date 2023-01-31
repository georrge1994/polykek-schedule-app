package com.android.feature.schedule.professor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.feature.schedule.R
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schedule.base.fragments.BaseScheduleFragment
import com.android.feature.schedule.databinding.FragmentProfessorScheduleBinding
import com.android.feature.schedule.professor.adapters.ProfessorLessonsRecyclerViewAdapter
import com.android.feature.schedule.professor.viewModels.ProfessorsScheduleViewModel

private const val PROFESSOR_ID = "PROFESSOR_ID"
private const val PROFESSOR_TITLE = "PROFESSOR"

/**
 * Professor schedule fragment.
 *
 * @constructor Create empty constructor for professor schedule fragment
 */
internal class ProfessorScheduleFragment : BaseScheduleFragment<ProfessorsScheduleViewModel>(ProfessorsScheduleViewModel::class) {
    private val viewBinding by viewBinding(FragmentProfessorScheduleBinding::bind)
    private lateinit var adapter: ProfessorLessonsRecyclerViewAdapter

    private val weekTitleObserver = Observer<String> { viewBinding.scheduleToolbar.weekName.text = it }

    private val lessonsObserver = Observer<List<Any>?> { adapter.updateItems(it) }

    private val lessonsIsEmptyObserver = Observer<Boolean> { viewBinding.lessonsListIsEmpty.isVisible = it }

    private val loadingObserver = Observer<Boolean> { viewBinding.animation.root.isVisible = it }

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateProfessorSchedule(arguments?.getInt(PROFESSOR_ID))
        adapter = ProfessorLessonsRecyclerViewAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_professor_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkAndRefresh()
        viewBinding.toolbarLayout.toolbar.updateToolbar(
            getString(R.string.professors_fragment_schedule_of, arguments?.getString(PROFESSOR_TITLE)),
            true
        )
        viewBinding.recyclerView.adapter = adapter

        viewModel.lessons.observe(viewLifecycleOwner, lessonsObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.weekTitle.observe(viewLifecycleOwner, weekTitleObserver)
        viewModel.listIsEmpty.observe(viewLifecycleOwner, lessonsIsEmptyObserver)

        viewBinding.scheduleToolbar.nextBtn.setOnClickListener(showNextWeek)
        viewBinding.scheduleToolbar.previousBtn.setOnClickListener(showPreviousWeek)
        viewBinding.scheduleToolbar.datesWrapper.setOnClickListener(showCalendar)
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }

    companion object {
        /**
         * Get bundle.
         *
         * @param id Professor id
         * @param title Professor title
         * @return [ProfessorScheduleFragment]
         */
        internal fun newInstance(id: Int, title: String?): ProfessorScheduleFragment = ProfessorScheduleFragment().apply {
            arguments = Bundle().apply {
                putInt(PROFESSOR_ID, id)
                putString(PROFESSOR_TITLE, title)
            }
        }
    }
}