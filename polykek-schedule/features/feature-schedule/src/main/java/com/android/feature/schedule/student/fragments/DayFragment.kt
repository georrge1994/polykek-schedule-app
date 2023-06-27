package com.android.feature.schedule.student.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.common.models.schedule.Lesson
import com.android.core.ui.fragments.NavigationFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.schedule.R
import com.android.feature.schedule.base.adapters.ILessonActions
import com.android.feature.schedule.base.dagger.IScheduleNavigationActions
import com.android.feature.schedule.base.dagger.ScheduleComponentHolder
import com.android.feature.schedule.databinding.FragmentDayBinding
import com.android.feature.schedule.student.adapters.recycler.LessonsRecyclerViewAdapter
import com.android.feature.schedule.student.mvi.StudentAction
import com.android.feature.schedule.student.mvi.StudentIntent
import com.android.feature.schedule.student.mvi.StudentState
import com.android.feature.schedule.student.viewModels.ScheduleWeekViewModel
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment
import javax.inject.Inject

private const val ID_DAY = "ID_DAY"

/**
 * Day fragment.
 *
 * @constructor Create empty constructor for day fragment
 * @author darkt on 8/24/2017
 */
internal class DayFragment : NavigationFragment<StudentIntent, StudentState, StudentAction, ScheduleWeekViewModel>() {
    private val viewBinding by viewBinding(FragmentDayBinding::bind)
    private lateinit var adapter: LessonsRecyclerViewAdapter
    private var dayId = 0

    @Inject
    lateinit var scheduleNavigationActions: IScheduleNavigationActions

    private val clickItemListener = object : ILessonActions {
        override fun onClick(lesson: Lesson) {
            StudentIntent.OpenNoteEditor(dayId, lesson.noteId, lesson.title).dispatchIntent()
        }
    }

    override fun getComponent(): IModuleComponent = ScheduleComponentHolder.getComponent()

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayId = arguments?.getInt(ID_DAY) ?: 0
        viewModel = createSharedViewModelWithParentFragment(viewModelFactory)
        adapter = LessonsRecyclerViewAdapter(requireContext(), clickItemListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_day, container, false)

    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedBeforeRendering(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun invalidateUi(state: StudentState) {
        super.invalidateUi(state)
        val lessonsForCurrentDay = state.days[dayId]?.lessons ?: emptyList()
        viewBinding.lessonsListIsEmpty.isVisible = lessonsForCurrentDay.isEmpty() && state.isLoading.not()
        adapter.updateItems(lessonsForCurrentDay)
    }

    override fun executeSingleAction(action: StudentAction) {
        super.executeSingleAction(action)
        if (action is StudentAction.OpenNoteEditor && action.dayId == dayId) {
            tabRouter?.navigateTo(
                PolytechFragmentScreen {
                    scheduleNavigationActions.getNoteEditorFragment(action.noteId, action.title)
                }
            )
        }
    }

    override fun onDestroyView() {
        adapter.detachRecyclerView()
        viewBinding.recyclerView.adapter = null
        super.onDestroyView()
    }

    companion object {
        /**
         * New instance.
         *
         * @param dayIndex Day index
         * @return [DayFragment]
         */
        internal fun newInstance(dayIndex: Int) = DayFragment().apply {
            arguments = Bundle().apply {
                putInt(ID_DAY, dayIndex)
            }
        }
    }
}