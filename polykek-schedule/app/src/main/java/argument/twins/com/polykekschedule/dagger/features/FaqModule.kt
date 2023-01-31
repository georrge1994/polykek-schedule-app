package argument.twins.com.polykekschedule.dagger.features

import androidx.fragment.app.Fragment
import argument.twins.com.polykekschedule.dagger.core.CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.feature.faq.dagger.IFaqModuleDependencies
import com.android.feature.faq.dagger.IFaqNavigationActions
import com.android.feature.feedback.dagger.FeedbackComponentHolder
import com.android.feature.feedback.dagger.IFeedbackModuleDependencies
import com.android.module.injector.dependenciesHolders.DependencyHolder1
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

internal const val FAQ_DYNAMIC_DEPENDENCIES_PROVIDER = "FAQ_DYNAMIC_DEPENDENCIES_PROVIDER"

@Module
class FaqModule {
    private class FaqModuleDependenciesHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        override val block: (IBaseDependencyHolder<IFaqModuleDependencies>, ICoreUiModuleApi) -> IFaqModuleDependencies
    ) : DependencyHolder1<ICoreUiModuleApi, IFaqModuleDependencies>(coreUiModuleApi)

    @Provides
    @Singleton
    @Named(FAQ_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideDynamicDependencyProviderForFaq(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(FEEDBACK_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForFeedback: DynamicProvider<IFeedbackModuleDependencies>
    ) = DynamicProvider {
        FaqModuleDependenciesHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi)
        ) { dependencyHolder, coreUiModuleApi ->
            object : IFaqModuleDependencies {
                override val faqNavigationActions: IFaqNavigationActions
                    get() = getFaqInnerNavigation(dynamicDependencyProviderForFeedback)
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }

    /**
     * Get faq inner navigation. We want to initialize Feedback api and module only if user clicks to getFeedbackScreen.
     *
     * @param dynamicDependencyProviderForFeedback Dynamic dependency provider for feedback
     * @return [IFaqNavigationActions]
     */
    private fun getFaqInnerNavigation(
        dynamicDependencyProviderForFeedback: DynamicProvider<IFeedbackModuleDependencies>
    ) = object : IFaqNavigationActions {
        override fun getFeedbackScreen(): Fragment = FeedbackComponentHolder.initAndGet(
            dynamicDependencyProviderForFeedback
        ).getFeedbackFragment()
    }
}