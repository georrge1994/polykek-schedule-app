package com.android.feature.schedule.base.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.schedule.professor.fragments.ProfessorScheduleFragment
import com.android.feature.schedule.student.fragments.DayFragment
import com.android.feature.schedule.student.fragments.ScheduleWeekFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IScheduleModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        ScheduleModuleInjector::class
    ]
)
internal abstract class ScheduleComponent : IModuleComponent, IScheduleModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: ProfessorScheduleFragment)
    internal abstract fun inject(fragment: ScheduleWeekFragment)
    internal abstract fun inject(fragment: DayFragment)

    override fun getProfessorScheduleFragment(id: Int, title: String?): Fragment = ProfessorScheduleFragment.newInstance(id, title)

    override fun getScheduleWeekFragment(): Fragment = ScheduleWeekFragment()

    companion object {
        /**
         * Create [ScheduleComponent].
         *
         * @param scheduleModuleDependencies Schedule module dependencies
         * @return [ScheduleComponent]
         */
        internal fun create(scheduleModuleDependencies: IScheduleModuleDependencies): ScheduleComponent =
            DaggerScheduleComponent.builder()
                .iScheduleModuleDependencies(scheduleModuleDependencies)
                .build()
    }
}