package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.groups.dagger.IGroupsModuleDependencies
import com.android.feature.groups.dagger.IGroupsNavigationActions
import com.android.feature.main.screen.dagger.IMainScreenModuleDependencies
import com.android.feature.main.screen.dagger.MainScreenComponentHolder
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val GROUPS_DYNAMIC_DEPENDENCIES_PROVIDER = "GROUPS_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class GroupsModule {
    private class GroupsModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IGroupsModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IGroupsModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IGroupsModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    @Provides
    @Singleton
    @Named(GROUPS_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForGroups(
        savedItemsRoomRepository: ISavedItemsRoomRepository, // This is from app module.
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreRetrofit: DynamicProvider<ICoreRetrofitDependencies>,
        @Named(MAIN_SCREEN_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForMainScreen: DynamicProvider<IMainScreenModuleDependencies>
    ) = DynamicProvider {
        GroupsModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            CoreRetrofitComponentHolder.initAndGet(dynamicDependencyProviderForCoreRetrofit)
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : IGroupsModuleDependencies {
                override val groupsNavigationActions: IGroupsNavigationActions
                    get() = getGroupsInnerNavigation(dynamicDependencyProviderForMainScreen)
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = savedItemsRoomRepository
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get groups inner navigation. Creates instance of main screen api by navigation action.
     *
     * @param dynamicDependencyProviderForMainScreen Dynamic dependency provider for main screen
     * @return [IGroupsNavigationActions]
     */
    private fun getGroupsInnerNavigation(
        dynamicDependencyProviderForMainScreen: DynamicProvider<IMainScreenModuleDependencies>
    ) = object : IGroupsNavigationActions {
        override fun getMainFragment(): Fragment =
            MainScreenComponentHolder.initAndGet(dynamicDependencyProviderForMainScreen).getMainFragment()
    }
}