package com.android.feature.feedback.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Feedback module api.
 */
interface IFeedbackModuleApi : IModuleApi {
    /**
     * Get feedback fragment.
     *
     * @return [Fragment]
     */
    fun getFeedbackFragment(): Fragment
}