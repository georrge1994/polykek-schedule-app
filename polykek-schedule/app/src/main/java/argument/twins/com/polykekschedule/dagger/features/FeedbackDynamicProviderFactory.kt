package argument.twins.com.polykekschedule.dagger.features

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.feature.feedback.dagger.FeedbackComponentHolder
import com.android.feature.feedback.dagger.IFeedbackModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import javax.inject.Inject

/**
 * Feedback module.
 *
 * @constructor Create empty constructor for feedback module
 */
class FeedbackDynamicProviderFactory @Inject constructor() :
    DynamicDependenciesProviderFactory<FeedbackComponentHolder, IFeedbackModuleDependencies>(FeedbackComponentHolder) {
    private class FeedbackModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IFeedbackModuleDependencies>, ICoreUiModuleApi) -> IFeedbackModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IFeedbackModuleDependencies>(coreUiModuleApi)

    override fun getDynamicProvider(): DynamicProvider<IFeedbackModuleDependencies> = DynamicProvider {
        FeedbackModuleDependenciesHolder(CoreUiComponentHolder.getApi()) { dependencyHolder, coreUiModuleApi ->
            object : IFeedbackModuleDependencies {
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}