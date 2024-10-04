package com.example.news.dagger

import androidx.fragment.app.Fragment

interface INewsNavigationActions {
    /**
     * Get web content screen.
     *
     * @return [Fragment]
     */
    fun getWebContentScreen(title: String, url: String): Fragment
}