package com.android.feature.faq.dagger

import androidx.fragment.app.Fragment

/**
 * Navigation for FAQ fragment.
 */
interface IFaqNavigationActions {
    /**
     * Get feedback screen.
     *
     * @return [Fragment]
     */
    fun getFeedbackScreen(): Fragment
}