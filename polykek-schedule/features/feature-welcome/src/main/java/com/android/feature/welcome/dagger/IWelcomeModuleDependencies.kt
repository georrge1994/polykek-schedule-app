package com.android.feature.welcome.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies

/**
 * Welcome module dependencies.
 */
interface IWelcomeModuleDependencies : ICoreUiModuleDependencies {
    val welcomeNavigationActions: IWelcomeNavigationActions
}