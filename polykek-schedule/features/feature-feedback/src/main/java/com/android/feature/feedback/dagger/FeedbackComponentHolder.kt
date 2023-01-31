package com.android.feature.feedback.dagger

import com.android.module.injector.ComponentHolder
import com.android.module.injector.WeakComponentHolderDelegate

/**
 * Feedback screen component holder.
 */
object FeedbackComponentHolder : ComponentHolder<IFeedbackModuleApi, IFeedbackModuleDependencies>() {
    private val componentHolderDelegate = WeakComponentHolderDelegate<IFeedbackModuleApi,
            IFeedbackModuleDependencies, FeedbackComponent> { FeedbackComponent.create(it) }

    override fun getApi(): IFeedbackModuleApi = componentHolderDelegate.getApi(dependenciesProvider)

    /**
     * Get component.
     *
     * @return [FeedbackComponent]
     */
    internal fun getComponent(): FeedbackComponent = getApi() as FeedbackComponent
}