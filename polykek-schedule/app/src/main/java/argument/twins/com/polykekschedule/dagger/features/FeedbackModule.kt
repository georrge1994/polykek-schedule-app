package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.feedback.dagger.FeedbackComponentHolder
import com.android.feature.feedback.dagger.IFeedbackModuleApi
import com.android.feature.feedback.dagger.IFeedbackModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val FEEDBACK_DYNAMIC_DEPENDENCIES_PROVIDER = "FEEDBACK_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Feedback module.
 *
 * @constructor Create empty constructor for feedback module
 */
@Module
class FeedbackModule {
    private class FeedbackModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IFeedbackModuleDependencies>, ICoreUiModuleApi) -> IFeedbackModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IFeedbackModuleDependencies>(coreUiModuleApi)

    @Provides
    @Singleton
    @Named(FEEDBACK_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForFeedback(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>
    ) = DynamicProvider {
        FeedbackModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi)
        ) { dependencyHolder, coreUiModuleApi ->
            object : IFeedbackModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}