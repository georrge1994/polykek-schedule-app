package com.android.feature.schedule.student.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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
import com.android.feature.schedule.student.viewModels.ScheduleWeekViewModel
import com.android.shared.code.utils.syntaxSugar.createSharedViewModelWithParentFragment
import javax.inject.Inject

private const val ID_DAY = "ID_DAY"

/**
 * Day fragment.
 *
 * @constructor Create empty constructor for day fragment
 * @author darkt on 8/24/2017
 */
internal class DayFragment : NavigationFragment() {
    private val viewBinding by viewBinding(FragmentDayBinding::bind)
    private lateinit var scheduleViewModel: ScheduleWeekViewModel
    private lateinit var adapter: LessonsRecyclerViewAdapter
    private var dayId = 0

    @Inject
    lateinit var scheduleNavigationActions: IScheduleNavigationActions

    private val dayObserver = Observer<List<Lesson>> { lessons ->
        viewBinding.lessonsListIsEmpty.isVisible = lessons.isEmpty()
        adapter.updateItems(lessons)
    }

    private val clickItemListener = object : ILessonActions {
        override fun onClick(lesson: Lesson) {
            tabRouter?.navigateTo(
                PolytechFragmentScreen {
                    scheduleNavigationActions.getNoteEditorFragment(lesson.noteId, lesson.title)
                }
            )
        }
    }

    override fun injectToComponent() = ScheduleComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply { dayId = getInt(ID_DAY) }
        adapter = LessonsRecyclerViewAdapter(requireContext(), clickItemListener)
        scheduleViewModel = createSharedViewModelWithParentFragment(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_day, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
        scheduleViewModel.getLessonsLiveData(dayId).observe(viewLifecycleOwner, dayObserver)
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