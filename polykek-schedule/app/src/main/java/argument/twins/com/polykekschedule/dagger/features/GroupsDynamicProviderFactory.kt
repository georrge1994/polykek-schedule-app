package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.groups.dagger.GroupsComponentHolder
import com.android.feature.groups.dagger.IGroupsModuleDependencies
import com.android.feature.groups.dagger.IGroupsNavigationActions
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Groups dynamic provider factory. Injected class have to be part of app-core.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [GroupsDynamicProviderFactory]
 */
class GroupsDynamicProviderFactory @Inject constructor(
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : DynamicDependenciesProviderFactory<GroupsComponentHolder, IGroupsModuleDependencies>(GroupsComponentHolder) {
    private class GroupsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IGroupsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IGroupsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IGroupsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    /**
     * Get groups inner navigation. Creates instance of main screen api by navigation action.
     */
    private val groupsNavigationActions: IGroupsNavigationActions = object : IGroupsNavigationActions {
        override fun getMainFragment(): Fragment = MainScreenComponentHolder.getApi().getMainFragment()
    }

    override fun getDynamicProvider(): DynamicProvider<IGroupsModuleDependencies> = DynamicProvider {
        GroupsModuleDependenciesHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : IGroupsModuleDependencies {
                override val groupsNavigationActions: IGroupsNavigationActions
                    get() = this@GroupsDynamicProviderFactory.groupsNavigationActions
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = this@GroupsDynamicProviderFactory.savedItemsRoomRepository
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}