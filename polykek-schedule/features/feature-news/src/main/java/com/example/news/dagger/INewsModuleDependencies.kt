package com.example.news.dagger

import com.android.core.ui.dagger.ICoreUiModuleDependencies
import retrofit2.Retrofit

/**
 *  Input dependencies for the [NewsComponent].
 */
interface INewsModuleDependencies : ICoreUiModuleDependencies {
    val newsNavigationActions: INewsNavigationActions
    val retrofit: Retrofit
}