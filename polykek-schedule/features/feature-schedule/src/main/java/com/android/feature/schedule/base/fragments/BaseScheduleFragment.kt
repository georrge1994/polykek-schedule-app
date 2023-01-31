package com.android.feature.schedule.base.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.core.ui.fragments.ToolbarFragment
import com.android.feature.schedule.base.viewModels.BaseScheduleViewModel
import kotlin.reflect.KClass

/**
 * Schedule base fragment.
 *
 * @param T Descendant viewModel type
 * @property clazz [KClass] to get java.class
 * @constructor Create [BaseScheduleFragment]
 */
internal abstract class BaseScheduleFragment<T : BaseScheduleViewModel>(private val clazz: KClass<T>) : ToolbarFragment() {
    protected lateinit var viewModel: T

    protected val showNextWeek = View.OnClickListener { viewModel.showNextWeekAsync() }

    protected val showPreviousWeek = View.OnClickListener { viewModel.showPreviousWeekAsync() }

    protected val showCalendar = View.OnClickListener { context?.showDataPicker() }

    private val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        viewModel.showSpecificDateAsync(year, month, day)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            store = viewModelStore,
            factory = viewModelFactory,
            defaultCreationExtras = defaultViewModelCreationExtras
        )[clazz.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asyncSubscribe()
    }

    /**
     * Show data picker.
     *
     * @receiver [Context]
     */
    private fun Context.showDataPicker() = with(viewModel.getDatePickerInfoForSelectedDate()) {
        DatePickerDialog(this@showDataPicker, datePickerListener, year, month, day).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unSubscribe()
    }
}