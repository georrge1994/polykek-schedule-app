package com.android.feature.feedback.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.dagger.CoreUiForFeatureModules
import com.android.feature.feedback.fragments.FeedbackFragment
import com.android.module.injector.moduleMarkers.IModuleComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IFeedbackModuleDependencies::class],
    modules = [
        CoreUiForFeatureModules::class,
        FeedbackModuleInjector::class,
    ],
)
internal abstract class FeedbackComponent : IModuleComponent, IFeedbackModuleApi {
    // Fragments injections.
    internal abstract fun inject(fragment: FeedbackFragment)

    override fun getFeedbackFragment(): Fragment = FeedbackFragment()

    companion object {
        /**
         * Create [FeedbackComponent].
         *
         * @param feedbackModuleDependencies Feedback module dependencies
         * @return [FeedbackComponent]
         */
        internal fun create(feedbackModuleDependencies: IFeedbackModuleDependencies): FeedbackComponent =
            DaggerFeedbackComponent.builder()
                .iFeedbackModuleDependencies(feedbackModuleDependencies)
                .build()
    }
}