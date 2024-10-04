package com.android.feature.groups.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.core.ui.models.ScheduleMode
import com.android.feature.groups.fragments.GroupListFragment
import com.android.feature.groups.fragments.GroupsFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IGroupsModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        GroupsModuleInjector::class
    ]
)
internal abstract class GroupsComponent : IModuleComponent, IGroupsModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: GroupsFragment)
    internal abstract fun inject(fragment: GroupListFragment)

    override fun getGroupsFragment(): Fragment = GroupsFragment()

    override fun getGroupsFragment(scheduleMode: ScheduleMode, schoolId: String, abbr: String) =
        GroupsFragment.newInstance(scheduleMode, schoolId, abbr)

    internal companion object {
        /**
         * Create [GroupsComponent].
         *
         * @param groupsModuleDependencies Groups module dependencies
         * @return [GroupsComponent]
         */
        internal fun create(groupsModuleDependencies: IGroupsModuleDependencies): GroupsComponent =
            DaggerGroupsComponent.builder()
                .iGroupsModuleDependencies(groupsModuleDependencies)
                .build()
    }
}