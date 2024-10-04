package com.example.news.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * News api module.
 */
interface INewsModuleApi : IModuleApi {
    /**
     * Get news screen.
     *
     * @return [Fragment]
     */
    fun getNewsFragment(): Fragment
}