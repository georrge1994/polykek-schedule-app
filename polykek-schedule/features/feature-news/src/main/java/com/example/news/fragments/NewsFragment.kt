package com.example.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.android.core.ui.fragments.NavigationComposeFragment
import com.android.core.ui.navigation.polytechCicirone.PolytechFragmentScreen
import com.android.feature.news.R
import com.android.module.injector.moduleMarkers.IModuleComponent
import com.android.shared.code.utils.syntaxSugar.createViewModel
import com.example.news.dagger.INewsNavigationActions
import com.example.news.dagger.NewsComponentHolder
import com.example.news.mvi.NewsAction
import com.example.news.mvi.NewsIntent
import com.example.news.mvi.NewsState
import com.example.news.ui.NewsScreen
import com.example.news.viewModels.NewsViewModel
import javax.inject.Inject

/**
 * Last 20 news from RSS.
 */
internal class NewsFragment : NavigationComposeFragment<NewsIntent, NewsState, NewsAction, NewsViewModel>() {
    @Inject
    lateinit var newsNavigationActions: INewsNavigationActions

    override fun getComponent(): IModuleComponent = NewsComponentHolder.getComponent()

    override fun injectToComponent(): Unit = NewsComponentHolder.getComponent().inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(requireContext()).apply {
            setContent {
                NewsScreen(
                    viewModel = viewModel,
                    dispatchIntent = { dispatchIntent() },
                )
            }
        }

    override fun executeSingleAction(action: NewsAction) {
        when (action) {
            is NewsAction.OpenItem -> openWebContentScreen(
                title = getString(R.string.news_fragment_news, action.position),
                url = action.url,
            )
        }
    }

    /**
     * Open web content fragment.
     */
    private fun openWebContentScreen(title: String, url: String) = tabRouter?.navigateTo(
        PolytechFragmentScreen {
            newsNavigationActions.getWebContentScreen(title, url)
        }
    )
}